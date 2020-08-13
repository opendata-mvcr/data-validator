@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  data-validator-cli startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and DATA_VALIDATOR_CLI_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\data-validator-cli-1.0.0.jar;%APP_HOME%\lib\data-validator-json-1.0.0.jar;%APP_HOME%\lib\data-validator-xml-1.0.0.jar;%APP_HOME%\lib\data-validator-rdf-1.0.0.jar;%APP_HOME%\lib\data-validator-core-1.0.0.jar;%APP_HOME%\lib\logback-json-classic-0.1.5.jar;%APP_HOME%\lib\logback-classic-1.2.3.jar;%APP_HOME%\lib\rdf4j-rio-turtle-3.2.3.jar;%APP_HOME%\lib\rdf4j-rio-datatypes-3.2.3.jar;%APP_HOME%\lib\rdf4j-rio-languages-3.2.3.jar;%APP_HOME%\lib\rdf4j-rio-api-3.2.3.jar;%APP_HOME%\lib\rdf4j-model-3.2.3.jar;%APP_HOME%\lib\rdf4j-util-3.2.3.jar;%APP_HOME%\lib\jena-tdb-3.15.0.jar;%APP_HOME%\lib\jena-shacl-3.15.0.jar;%APP_HOME%\lib\jena-arq-3.15.0.jar;%APP_HOME%\lib\jsonld-java-0.12.5.jar;%APP_HOME%\lib\jsonld-java-0.12.5.jar;%APP_HOME%\lib\libthrift-0.13.0.jar;%APP_HOME%\lib\jcl-over-slf4j-1.7.30.jar;%APP_HOME%\lib\jena-core-3.15.0.jar;%APP_HOME%\lib\jena-base-3.15.0.jar;%APP_HOME%\lib\slf4j-api-1.7.30.jar;%APP_HOME%\lib\logback-jackson-0.1.5.jar;%APP_HOME%\lib\commons-cli-1.4.jar;%APP_HOME%\lib\jackson-dataformat-yaml-2.11.1.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.11.1.jar;%APP_HOME%\lib\jackson-dataformat-xml-2.11.1.jar;%APP_HOME%\lib\jackson-module-jaxb-annotations-2.11.1.jar;%APP_HOME%\lib\jackson-databind-2.11.1.jar;%APP_HOME%\lib\org.everit.json.schema-1.12.1.jar;%APP_HOME%\lib\org.eclipse.wst.xml.xpath2.processor-1.1.5-738bb7b85d.jar;%APP_HOME%\lib\xercesImpl-xsd11-2.12-beta-r1667115.jar;%APP_HOME%\lib\titanium-json-ld-0.8.4.jar;%APP_HOME%\lib\jakarta.json-1.1.6.jar;%APP_HOME%\lib\jaxb-api-2.3.1.jar;%APP_HOME%\lib\logback-json-core-0.1.5.jar;%APP_HOME%\lib\logback-core-1.2.3.jar;%APP_HOME%\lib\commons-io-2.6.jar;%APP_HOME%\lib\jackson-annotations-2.11.1.jar;%APP_HOME%\lib\jackson-core-2.11.1.jar;%APP_HOME%\lib\snakeyaml-1.26.jar;%APP_HOME%\lib\json-20190722.jar;%APP_HOME%\lib\commons-validator-1.6.jar;%APP_HOME%\lib\handy-uri-templates-2.1.8.jar;%APP_HOME%\lib\re2j-1.3.jar;%APP_HOME%\lib\woodstox-core-6.2.1.jar;%APP_HOME%\lib\stax2-api-4.2.1.jar;%APP_HOME%\lib\xml-apis-1.4.01.jar;%APP_HOME%\lib\javax.activation-api-1.2.0.jar;%APP_HOME%\lib\commons-digester-1.8.1.jar;%APP_HOME%\lib\httpclient-cache-4.5.10.jar;%APP_HOME%\lib\httpclient-4.5.10.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\commons-collections-3.2.2.jar;%APP_HOME%\lib\joda-time-2.10.2.jar;%APP_HOME%\lib\jakarta.xml.bind-api-2.3.2.jar;%APP_HOME%\lib\jakarta.activation-api-1.2.1.jar;%APP_HOME%\lib\java-cup-10k.jar;%APP_HOME%\lib\jena-shaded-guava-3.15.0.jar;%APP_HOME%\lib\commons-lang3-3.10.jar;%APP_HOME%\lib\jena-iri-3.15.0.jar;%APP_HOME%\lib\httpcore-4.4.12.jar;%APP_HOME%\lib\commons-codec-1.14.jar;%APP_HOME%\lib\javax.annotation-api-1.3.2.jar;%APP_HOME%\lib\commons-csv-1.8.jar;%APP_HOME%\lib\commons-compress-1.20.jar;%APP_HOME%\lib\collection-0.7.jar


@rem Execute data-validator-cli
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %DATA_VALIDATOR_CLI_OPTS%  -classpath "%CLASSPATH%" cz.mvcr.datavalidator.cli.AppEntry %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable DATA_VALIDATOR_CLI_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%DATA_VALIDATOR_CLI_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
