# Build and run TestDB with MySQL Connector/J
$ErrorActionPreference = "Stop"
$root = $PSScriptRoot
Set-Location $root

$jar = Join-Path $root "lib\mysql-connector-j-8.4.0.jar"
if (-not (Test-Path $jar)) {
    Write-Host "Missing: $jar"
    Write-Host "Download from Maven: com.mysql:mysql-connector-j:8.4.0"
    exit 1
}

New-Item -ItemType Directory -Path (Join-Path $root "out") -Force | Out-Null

$sources = Get-ChildItem -Path (Join-Path $root "src") -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
& javac -encoding UTF-8 -d (Join-Path $root "out") -sourcepath (Join-Path $root "src") @sources
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

& javac -encoding UTF-8 -d (Join-Path $root "out") -cp "$root\out;$jar" (Join-Path $root "TestDB.java")
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Host ""
& java -cp "$root\out;$jar" TestDB
