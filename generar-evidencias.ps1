# =====================================================================================
#  Genera TODAS las evidencias del taller en la carpeta evidencias/:
#   1. Corre la suite principal (BlazeDemoRunner)   -> reporte Serenity + log
#   2. Corre los defectos conocidos (KnownIssuesRunner) -> reporte Serenity + log
#   3. Un GIF animado por escenario (paso a paso) para cada reporte
#   4. Capturas PNG de las páginas clave del reporte (Chrome headless)
#  Uso (PowerShell, desde la raíz del proyecto):  .\generar-evidencias.ps1
# =====================================================================================
$ErrorActionPreference = 'Continue'
Set-Location $PSScriptRoot
$ev = Join-Path $PSScriptRoot 'evidencias'
$chrome = 'C:\Program Files\Google\Chrome\Application\chrome.exe'
New-Item -ItemType Directory -Force "$ev\reportes", "$ev\videos", "$ev\capturas", "$ev\logs" | Out-Null

function Correr-Suite($runner, $nombre) {
    Write-Host "== Ejecutando $runner ..."
    .\gradlew.bat clean test "-Prunner=$runner" --console=plain *> "$ev\logs\corrida-$nombre.log"
    Select-String -Path "$ev\logs\corrida-$nombre.log" -Pattern ' PASSED| FAILED|tests completed|BUILD' | ForEach-Object { $_.Line }
    $destino = "$ev\reportes\$nombre"
    if (Test-Path $destino) { Remove-Item -Recurse -Force $destino }
    Copy-Item -Recurse 'target\site\serenity' $destino
    $videos = "$ev\videos\$nombre"
    if (Test-Path $videos) { Remove-Item -Recurse -Force $videos }
    .\gradlew.bat generarVideos "-PreporteDir=evidencias/reportes/$nombre" "-PvideosDir=evidencias/videos/$nombre" --console=plain | Select-String 'Total'
}

# Página de detalle de un escenario: misma base que su .json
function Pagina-De($dir, $titulo) {
    foreach ($j in Get-ChildItem "$dir\*.json") {
        $texto = Get-Content $j.FullName -Raw -Encoding UTF8
        if ($texto -match ('"title":"(?:[^"\\]|\\.)*' + [regex]::Escape($titulo))) {
            $html = $j.FullName -replace '\.json$', '.html'
            if (Test-Path $html) { return $html }
        }
    }
    return $null
}

function Capturar($archivo, $pagina, $alto) {
    if (-not $pagina) { Write-Host "  (sin pagina para $archivo)"; return }
    $url = 'file:///' + ($pagina -replace '\\', '/')
    & $chrome --headless=new --disable-gpu --hide-scrollbars "--window-size=1440,$alto" --virtual-time-budget=10000 "--screenshot=$ev\capturas\$archivo" $url 2>$null | Out-Null
    Write-Host "  captura: $archivo"
}

Correr-Suite 'BlazeDemoRunner' 'suite-principal'
Correr-Suite 'KnownIssuesRunner' 'defectos-conocidos'

$sp = "$ev\reportes\suite-principal"
$dc = "$ev\reportes\defectos-conocidos"
Write-Host '== Capturas del reporte'
Capturar '01_reporte_resumen_suite_principal.png' "$sp\index.html" 1800
Capturar '02_escenario_ruta_feliz_E2E.png' (Pagina-De $sp 'Compra exitosa de un vuelo') 3600
Capturar '03_scenario_outline_tipos_de_tarjeta.png' (Pagina-De $sp 'Compra con distintos tipos de tarjeta') 3600
Capturar '04_scenario_outline_busqueda_rutas.png' (Pagina-De $sp 'squeda de vuelos para varias rutas') 3600
Capturar '05_hallazgo_formulario_vacio.png' (Pagina-De $sp 'Compra con el formulario vac') 3000
Capturar '06_hallazgo_servidor_acepta_origen_igual_destino.png' (Pagina-De $sp 'Origen y destino iguales') 2400
Capturar '07_resumen_defectos_conocidos.png' "$dc\index.html" 1800
Capturar '08_defecto_precio_compra_distinto.png' (Pagina-De $dc 'El precio de la compra coincide') 2600
Capturar '09_defecto_monto_confirmacion.png' (Pagina-De $dc 'El monto de la confirmaci') 3000
Capturar '10_defecto_tarjeta_confirmacion.png' (Pagina-De $dc 'La confirmaci') 3000
Capturar '11_hallazgo_inyeccion_html.png' (Pagina-De $sp 'Inyecci') 2400
Capturar '12_hallazgo_confirmacion_sin_compra.png' (Pagina-De $sp 'Abrir la confirmaci') 2400
Capturar '13_hallazgo_home_lleva_a_login.png' (Pagina-De $sp 'lleva al inicio de sesi') 2400
Capturar '14_cuenta_campos_obligatorios_registro.png' (Pagina-De $sp 'El registro exige') 2400
Capturar '15_outline_cualquier_vuelo.png' (Pagina-De $sp 'Cualquier vuelo de la lista') 3600
Capturar '16_dos_compras_ids_distintos.png' (Pagina-De $sp 'Dos compras seguidas') 3000

# Comprimir los reportes para entregarlos
Compress-Archive -Force -Path $sp -DestinationPath "$ev\reporte-serenity-suite-principal.zip"
Compress-Archive -Force -Path $dc -DestinationPath "$ev\reporte-serenity-defectos-conocidos.zip"
Write-Host '== Evidencias listas en' $ev
