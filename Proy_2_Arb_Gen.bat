@echo off
setlocal

:: Verificar si Proy_2_Arb_Gen.jar está en el directorio actual o en dist
if exist "Proy_2_Arb_Gen.jar" (
    set JAR_PATH=Proy_2_Arb_Gen.jar
) else if exist "dist\Proy_2_Arb_Gen.jar" (
    set JAR_PATH=dist\Proy_2_Arb_Gen.jar
) else (
    echo Proy_2_Arb_Gen.jar no encontrado ni en el directorio actual ni en el subdirectorio dist.
    endlocal
    exit /b 1
)

:: Ejecutar el JAR en una nueva ventana y cerrar la ventana de cmd
start javaw -jar %JAR_PATH%

endlocal
exit