// Teste de ponta a ponta (Playwright) da Livraria A3.
// Pré-requisito: servidor recém-iniciado em http://localhost:8080
async (page) => {
  const r = [];
  const check = (nome, cond) => r.push((cond ? 'PASS ' : 'FAIL ') + nome);
  const base = 'http://localhost:8080';
  const alerta = async () => (await page.locator('[role=alert]').textContent().catch(() => '')) || '';
  const status = async () => (await page.locator('[role=status]').textContent().catch(() => '')) || '';
  const celula = (nome) => page.getByRole('cell', { name: nome, exact: true });
  const estado = (id) => page.locator('#pedido-' + id + ' .estado').textContent();
  const estoqueDe = async (busca) => {
    await page.goto(base + '/produtos?busca=' + busca);
    return page.locator('table tr').nth(1).locator('td').nth(3).textContent();
  };

  // Produtos: consulta e cadastro com validações
  await page.goto(base + '/');
  check('lista 3 produtos iniciais', (await page.locator('table tr').count()) === 4);
  await page.getByRole('button', { name: 'Cadastrar' }).click();
  check('valida nome obrigatório', (await alerta()).includes('Nome é obrigatório'));
  await page.getByLabel('Nome').fill('Livro X');
  await page.getByLabel('Preço').fill('-5');
  await page.getByLabel('Estoque').fill('2');
  await page.getByRole('button', { name: 'Cadastrar' }).click();
  check('valida preço > 0', (await alerta()).includes('maior que zero'));
  await page.getByLabel('Nome').fill('Livro X');
  await page.getByLabel('Preço').fill('abc');
  await page.getByLabel('Estoque').fill('2');
  await page.getByRole('button', { name: 'Cadastrar' }).click();
  check('valida preço numérico', (await alerta()).includes('deve ser um número'));
  await page.getByLabel('Nome').fill('Java Efetivo');
  await page.getByLabel('Preço').fill('100,00');
  await page.getByLabel('Estoque').fill('2');
  await page.getByRole('button', { name: 'Cadastrar' }).click();
  check('cadastra produto', (await status()).includes('cadastrado') && await celula('Java Efetivo').isVisible());
  await page.getByLabel('Nome').fill('java efetivo');
  await page.getByLabel('Preço').fill('10');
  await page.getByLabel('Estoque').fill('1');
  await page.getByRole('button', { name: 'Cadastrar' }).click();
  check('bloqueia nome duplicado', (await alerta()).includes('Já existe'));
  await page.getByPlaceholder('Buscar produto').fill('java');
  await page.getByRole('button', { name: 'Buscar' }).click();
  check('busca por nome', (await page.locator('table tr').count()) === 2);
  await page.getByPlaceholder('Buscar produto').fill('inexistente');
  await page.getByRole('button', { name: 'Buscar' }).click();
  check('busca sem resultado', await page.getByText('Nenhum produto encontrado').isVisible());

  // Carrinho
  await page.goto(base + '/carrinho');
  check('carrinho vazio exibido', await page.getByText('Seu carrinho está vazio').isVisible());
  await page.goto(base + '/produtos');
  await page.getByLabel('Quantidade de Java Efetivo').fill('0');
  await page.getByRole('button', { name: 'Adicionar Java Efetivo ao carrinho' }).click();
  check('valida quantidade > 0', (await alerta()).includes('maior que zero'));
  await page.getByLabel('Quantidade de Java Efetivo').fill('5');
  await page.getByRole('button', { name: 'Adicionar Java Efetivo ao carrinho' }).click();
  check('valida estoque ao adicionar', (await alerta()).includes('Estoque insuficiente'));
  await page.getByLabel('Quantidade de Java Efetivo').fill('2');
  await page.getByRole('button', { name: 'Adicionar Java Efetivo ao carrinho' }).click();
  await page.getByLabel('Quantidade de Dom Casmurro').fill('1');
  await page.getByRole('button', { name: 'Adicionar Dom Casmurro ao carrinho' }).click();
  check('contador do carrinho = 3', await page.getByRole('link', { name: 'Carrinho (3)' }).isVisible());
  await page.goto(base + '/carrinho');
  await page.getByRole('button', { name: 'Remover Dom Casmurro' }).click();
  check('remove item', (await status()).includes('removido') && !(await celula('Dom Casmurro').isVisible()));
  const txt = await page.textContent('body');
  check('subtotal R$ 200,00', txt.includes('Subtotal: R$ 200,00'));
  check('Strategy Pix = R$ 180,00', txt.includes('Pix (10% de desconto) — total R$ 180,00'));
  check('Strategy Cartão = R$ 200,00', txt.includes('Cartão de crédito — total R$ 200,00'));
  check('Strategy Boleto = R$ 203,00', txt.includes('Boleto (taxa de R$ 3,00) — total R$ 203,00'));
  await page.getByRole('button', { name: 'Finalizar pedido' }).click();
  check('exige forma de pagamento', (await alerta()).includes('forma de pagamento'));

  // Finalização e estados do pedido
  await page.getByLabel(/Pix/).check();
  await page.getByRole('button', { name: 'Finalizar pedido' }).click();
  check('pedido #1 criado', (await status()).includes('Pedido #1 realizado'));
  check('estado inicial = Aguardando pagamento', (await estado(1)) === 'Aguardando pagamento');
  check('total com desconto no pedido', (await page.locator('#pedido-1').textContent()).includes('R$ 180,00'));
  check('carrinho esvaziado', await page.getByRole('link', { name: 'Carrinho (0)' }).isVisible());
  check('estoque baixado (2 -> 0)', (await estoqueDe('java')) === '0');
  await page.goto(base + '/pedidos');
  await page.getByRole('button', { name: 'entregar pedido 1' }).click();
  check('State bloqueia entregar antes de pagar', (await alerta()).includes('Não é possível entregar'));
  await page.getByRole('button', { name: 'pagar pedido 1' }).click();
  check('Aguardando -> Pago', (await estado(1)) === 'Pago');
  await page.getByRole('button', { name: 'enviar pedido 1' }).click();
  check('Pago -> Enviado', (await estado(1)) === 'Enviado');
  await page.getByRole('button', { name: 'cancelar pedido 1' }).click();
  check('State bloqueia cancelar pedido enviado', (await alerta()).includes('Não é possível cancelar'));
  await page.getByRole('button', { name: 'entregar pedido 1' }).click();
  check('Enviado -> Entregue', (await estado(1)) === 'Entregue');
  check('Observer gerou 3 notificações', (await page.locator('#notificacoes li').count()) === 3);

  // Cancelamento devolve estoque (Observer)
  await page.goto(base + '/produtos');
  await page.getByLabel('Quantidade de Clean Code').fill('3');
  await page.getByRole('button', { name: 'Adicionar Clean Code ao carrinho' }).click();
  await page.goto(base + '/carrinho');
  await page.getByLabel(/Boleto/).check();
  await page.getByRole('button', { name: 'Finalizar pedido' }).click();
  check('total boleto = R$ 270,00', (await page.locator('#pedido-2').textContent()).includes('R$ 270,00'));
  const antes = await estoqueDe('clean');
  await page.goto(base + '/pedidos');
  await page.getByRole('button', { name: 'cancelar pedido 2' }).click();
  check('Aguardando -> Cancelado', (await estado(2)) === 'Cancelado');
  const depois = await estoqueDe('clean');
  check('Observer devolve estoque (' + antes + ' -> ' + depois + ')', antes === '0' && depois === '3');
  await page.goto(base + '/pedidos');
  await page.getByRole('button', { name: 'pagar pedido 2' }).click();
  check('State bloqueia pagar pedido cancelado', (await alerta()).includes('Não é possível pagar'));

  // Segurança e rotas
  await page.goto(base + '/produtos?busca=' + encodeURIComponent('<b>x</b>'));
  check('entrada do usuário escapada no HTML', (await page.locator('b').count()) === 0);
  const resp = await page.goto(base + '/produtos/nada');
  check('404 para rota inexistente', resp.status() === 404);

  const falhas = r.filter((l) => l.startsWith('FAIL')).length;
  return r.join('\n') + '\n\n' + (r.length - falhas) + '/' + r.length + ' passaram';
}
