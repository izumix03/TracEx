--------------------------------------------------
  TracEx

  by iz_j
--------------------------------------------------

[1. Introduction]
  
  ローカルPCで動かすTracサービスです。


[2. File organization]

  README.txt
  TracEx.properties      設定ファイル
  TracEx.exe             サービスのインストール・アンインストール用exe
  TracExw.exe            サービスの起動・停止用exe
  service-install.bat    インストール実行batファイル
  service-uninstall.bat  アンインストール実行batファイル
  standalone.bat         都度起動用batファイル

[3. Usage]

  TracEx.propertiesに適宜設定してください。
  
  server    ：接続先サーバー名
  databse   ：接続先サーバー名のデータベース名
  port      ：ローカルで使用するポート
  user      ：TracのユーザーID

  @Windows
  ・サービスとしてインストールする場合
  各自の環境に合わせて「install.bat」のJVM_PATHを書き換えてください。※Java7以上！
  install.batを起動するとサービスとして登録されます。
  インストール後は、Windowsのサービス管理ツールもしくはTracExw.exeからサービスを開始・停止してください。

  ・都度、起動する場合
  standalone.batをキックしてください。

  @Linux
  ･･･いつかそのうち

  起動後、ブラウザから
    http://localhost:8088/index
  へアクセスしてください。（ポートは設定による）

