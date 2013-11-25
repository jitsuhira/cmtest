# cmtest 

## これはなに？
JavaでJUnitなどを利用してテストコードを書く時に便利なヘルパーライブラリです。

## バージョン
現在のバージョンは0.3です。
詳細は[バージョン情報](./VERSION)を参照してください。

## ライセンス
Apache License Version 2.0です。

## モジュール構成

| モジュール名 | 概要 |
| ------ | ------ |
| [cmtest-core](./cmtest-core/) | JUnitを使ったテストを書くためのモジュールです。 |
| [cmtest-db](./cmtest-db/) | DbUnitを使ったデータベーステストを書くためのモジュールです。 |
| [cmtest-baseunits](./cmtest-baseunits/) | [BaseUnits Library](https://github.com/dai0304/baseunits/)を使ったテストを書くためのモジュールです。 |
| cmtest-examples | テストコードのサンプルです。 |


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
	  	    <version>0.3</version>
	  	    <scope>test</scope>
	  	</dependency>
	  	<dependency>
	  	    <groupId>jp.classmethod.testing</groupId>
	  	    <artifactId>cmtest-db</artifactId>
	  	    <version>0.3</version>
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
		testCompile 'jp.classmethod.testing:cmtest-core:0.3'
		testCompile 'jp.classmethod.testing:cmtest-db:0.3'
	}
```

## 依存ライブラリ
Java 1.7以上が必要です。

### cmtest-core
- [JUnit](http://junit.org/) 4.11+

### cmtest-db
- [JUnit](http://junit.org/) 4.11+
- [DbUnit](http://dbunit.sourceforge.net/) 2.4.9
- [SnakeYAML](https://code.google.com/p/snakeyaml/) 1.13

### cmtest-baseunits
- [JUnit](http://junit.org/) 4.11+
- [BaseUnits Library](https://github.com/dai0304/baseunits/) 2.11+

## 使い方
各モジュールのREADME.mdを参照してください。



