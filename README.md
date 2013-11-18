# cmtest 

## これはなに？
JavaでJUnitなどを利用してテストコードを書く時に便利なヘルパーライブラリです。

## バージョン
現在のバージョンは0.1-SNAPSHOTです。
ある程度のクラスを移植した段階で0.1にする予定です。

## ライセンス
Apache License Version 2.0です。

## モジュール構成

| モジュール名 | 説明 |
|:-|:-|:-|
| cmtest-core       | JUnitを使ったテストを書くためのモジュールです。 |
| cmtest-examples   | テストコードのサンプルです。 |

## インストール方法
Mavenリポジトリに公開しています。
Mavenを使っているならば、pom.xmlに追加してください。

```xml
	  <repositories>
	    <repository>
	      <id>cm.repos</id>
	      <name>classmethod repository release</name>
	      <url>http://public-maven.classmethod.info/release</url>
	    </repository>
	    <repository>
	      <id>cm.repos.snapshot</id>
	      <name>classmethod repository snapshot</name>
	      <url>http://public-maven.classmethod.info/snapshot</url>
	      <snapshots>
	        <enabled>true</enabled>
	      </snapshots>
	    </repository>
	  </repositories>
	  <dependencies>
	  	<dependency>
	  	    <groupId>jp.classmethod.testing</groupId>
	  	    <artifactId>cmtest-core</artifactId>
	  	    <version>0.1-SNAPSHOT</version>
	  	    <scope>test</scope>
	  	</dependency>
	  </dependencies>
```

Gradleを利用しているならば、build.gradleに追加します。

```groovy
	repositories {
		mavenCentral()
		maven { url 'http://public-maven.classmethod.info/release' }
		maven { url 'http://public-maven.classmethod.info/snapshot' }
	}
	dependencies {
		testCompile 'jp.classmethod.testing:cmtest-core:0.1-SNAPSHOT'
	}
```

## 依存ライブラリ
Java 1.7以上が必要です。

### cmtest-core
- JUnit 4.11

## 使い方
各モジュールのREADME.mdを参照してください。



