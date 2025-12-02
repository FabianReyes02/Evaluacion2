<#
setup-android-sdk.ps1

Este script busca rutas comunes del Android SDK, actualiza el archivo local.properties
con la ruta encontrada (formato escapado aceptado por Gradle) y establece las
variables de entorno de usuario ANDROID_HOME y añade platform-tools al PATH de usuario.

Ejecútalo en PowerShell (no requiere privilegios elevados si sólo modifica variables de usuario):
- Abre PowerShell
- Navega a la carpeta del proyecto: cd 'C:\Users\ibane\Desktop\Evaluacion2'
- Si es necesario, permite la ejecución: Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
- Ejecuta: .\setup-android-sdk.ps1
#>

function Escape-ForLocalProperties($p) {
    # Escapa ':' como '\:' y cada backslash '\' como '\\' para local.properties
    $r = $p -replace '(:)','\\$1'
    $r = $r -replace '\\','\\\\'
    return $r
}

$cwd = Split-Path -Path $MyInvocation.MyCommand.Path -Parent
Write-Host "Proyecto: $cwd"

$candidates = @()
if ($env:ANDROID_HOME -and (Test-Path $env:ANDROID_HOME)) { $candidates += $env:ANDROID_HOME }
$localAppSdk = Join-Path $env:LOCALAPPDATA "Android\Sdk"
$candidates += @(
    "C:\\Users\\$env:USERNAME\\AppData\\Local\\Android\\Sdk",
    $localAppSdk,
    "C:\\Android\\Sdk"
) | Get-Unique

$found = $null
foreach ($c in $candidates) {
    if ($null -ne $c -and $c -ne '' -and (Test-Path $c)) {
        Write-Host "SDK encontrado en: $c"
        $found = $c
        break
    }
}

if (-not $found) {
    Write-Host "No se encontró el Android SDK en rutas comunes."
    Write-Host "Rutas comprobadas:"; $candidates | ForEach-Object { Write-Host "  $_" }
    Write-Host "Si tienes el SDK en otra ruta, puedes ejecutar este script con la ruta como argumento:"
    Write-Host "  .\setup-android-sdk.ps1 -SdkPath 'C:\\ruta\\a\\Sdk'"
    exit 1
}

param([string]$SdkPath = $found)

if ($SdkPath -ne $found) {
    if (-not (Test-Path $SdkPath)) { Write-Host "La ruta indicada no existe: $SdkPath"; exit 1 }
}

# Escapar para local.properties
$escaped = Escape-ForLocalProperties($SdkPath)

# Escribir local.properties en la raíz del proyecto
$lpPath = Join-Path $cwd "local.properties"
$header = @(
    "## This file must *NOT* be checked into Version Control Systems,",
    "# as it contains information specific to your local configuration.",
    "# Location of the SDK. This is only used by Gradle.",
    "# Updated by setup-android-sdk.ps1",
    ""
)

$content = $header + ("sdk.dir=$escaped")
$content | Out-File -FilePath $lpPath -Encoding ASCII -Force
Write-Host "Wrote local.properties -> $lpPath"

# Establecer variable de entorno ANDROID_HOME a nivel de usuario
[Environment]::SetEnvironmentVariable('ANDROID_HOME',$SdkPath,'User')
Write-Host "ANDROID_HOME establecida (User) = $SdkPath"

# Añadir platform-tools al PATH de usuario si no existe
$platformTools = Join-Path $SdkPath 'platform-tools'
$userPath = [Environment]::GetEnvironmentVariable('Path','User')
if ($userPath -notlike "*${platformTools}*") {
    $newUserPath = $userPath
    if ($newUserPath -and $newUserPath -ne '') { $newUserPath += ";$platformTools" } else { $newUserPath = $platformTools }
    [Environment]::SetEnvironmentVariable('Path',$newUserPath,'User')
    Write-Host "Añadido al Path de usuario: $platformTools"
} else {
    Write-Host "platform-tools ya presente en Path de usuario"
}

# Comprobar archivos clave del SDK
if (Test-Path (Join-Path $SdkPath 'platforms')) { Write-Host "Carpeta 'platforms' existe." } else { Write-Host "Advertencia: no se encontró 'platforms' dentro de $SdkPath" }
if (Test-Path (Join-Path $platformTools 'adb.exe')) { Write-Host "adb encontrado en platform-tools." } else { Write-Host "adb no encontrado en platform-tools (puede que el SDK esté incompleto)." }

Write-Host "Listo. Reinicia tu terminal/Android Studio para que las variables de entorno tengan efecto."
