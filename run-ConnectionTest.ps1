$root = $PSScriptRoot
$jar = Join-Path $root "lib\mysql-connector-j-8.4.0.jar"
if (-not (Test-Path $jar)) {
    Write-Host "Missing JDBC jar: $jar"
    exit 1
}
Set-Location $root
New-Item -ItemType Directory -Path (Join-Path $root "out") -Force | Out-Null
$sources = Get-ChildItem -Path (Join-Path $root "src") -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
& javac -encoding UTF-8 -d (Join-Path $root "out") -sourcepath (Join-Path $root "src") @sources
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
Write-Host ""
& java -cp "$root\out;$jar" com.railway.db.DatabaseConnection
