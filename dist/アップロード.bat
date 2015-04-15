set CUR_DIR=%~dp0
set CUR_DIR=%CUR_DIR:~0,-1%
xcopy /d /y "%CUR_DIR%" "\\172.27.12.61\newac\doc\03.Cash\01_開発\work\tool\TracEx" /exclude:.\アップロード除外.txt

pause