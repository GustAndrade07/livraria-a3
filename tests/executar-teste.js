// Executa o teste de ponta a ponta no Chromium do Playwright.
// Pré-requisitos: Node.js, `npm install playwright` e `npx playwright install chromium`,
// e a aplicação recém-iniciada em http://localhost:8080.
const { chromium } = require('playwright');
const fluxoCompleto = require('./fluxo-completo');

async function main() {
  const navegador = await chromium.launch();
  try {
    const pagina = await navegador.newPage();
    const resultado = await fluxoCompleto(pagina);
    console.log(resultado);
    if (resultado.includes('FAIL')) {
      process.exitCode = 1;
    }
  } finally {
    await navegador.close();
  }
}

main().catch((erro) => {
  console.error(erro);
  process.exitCode = 1;
});
