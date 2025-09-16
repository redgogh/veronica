@REM --------------------------------------------------------------------------------
@REM
@REM    Copyright (C) 2019-2024 RedGogh All rights reserved.
@REM
@REM    Licensed under the Apache License, Version 2.0 (the "License");
@REM    you may not use this file except in compliance with the License.
@REM    You may obtain a copy of the License at
@REM
@REM        http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM    Unless required by applicable law or agreed to in writing, software
@REM    distributed under the License is distributed on an "AS IS" BASIS,
@REM    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM    See the License for the specific language governing permissions and
@REM    limitations under the License.
@REM
@REM --------------------------------------------------------------------------------

@REM ============================================================================
@REM 查找进程 ID
@REM   ./findnet [PORT]
@REM
@REM 如果需要根据端口杀死进程，添加 [-k] 参数
@REM  ./findnet -k [PORT]
@REM ============================================================================

@echo off
@chcp 65001 >nul
@setlocal

set IS_KILL=0

if "%1"=="-k" (
    set PORT=%2
    set IS_KILL=1
) else (
    set PORT=%1
)

for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%PORT%') do (
    set PID=%%a
    goto KILL
)

:KILL
if "%PID%"=="" (
    echo 未找到占用端口 %PORT% 的进程。
) else (
    if %IS_KILL%==1 (
        echo 正在杀死进程 PID: %PID%
        taskkill /PID %PID% /F
    ) else (
        echo 当前端口 %PORT% 正在占用 %PID% 进程。
    )
)
:END