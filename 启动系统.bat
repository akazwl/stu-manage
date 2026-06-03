@echo off
chcp 65001 >nul
echo ============================================
echo   山东大学软件学院 - 学生综合管理系统
echo ============================================
echo.
echo 正在编译最新代码...
cd /d "%~dp0"
mvnw clean compile -q
echo 编译完成，正在启动系统...
echo.
mvnw javafx:run
pause
