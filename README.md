# scala-functions
simple scala gcp cloud functions hello world example with sbt

mavenではなくsbtを使ってscalaのソースをgoogle cloud functionsにデプロイするサンプル

googleの公式ドキュメントではmavenを使ってfat .jarをビルドすることができると書かれているが、sbt-assembly pluginを使うことでsbtでもビルドすることができる

## guide

### use maven google cloud functions library with sbt

see https://cloud.google.com/functions/docs/concepts/jvm-langs

```xml
 <dependency>
      <groupId>com.google.cloud.functions</groupId>
      <artifactId>functions-framework-api</artifactId>
      <version>1.0.1</version>
      <scope>provided</scope>
    </dependency>
```

MavenのXMLはsbtのlibraryDependenciesと対応しているので以下のように変換する.

Maven's xml corresponds to sbt libraryDependencies.

Translate this like below.

```sbt
libraryDependencies ++= Seq(
  "com.google.cloud.functions" % "functions-framework-api" % "1.0.1",
  )
```
###  Install sbt-assembly plugin to build fat .jar file.

To deploy functions on java 11 runtime, fat .jar file is required.

Java 11 ランタイムで動くソースをアップロードする際にはfat .jarファイルが必要なのでsbt-assembly pluginを``project/plugins.sbt``に追加する

- add project/plugins.sbt 

```scala:plugins.sbt

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.15.0")

```

Run `assembly` command in sbt shell to build fat .jar file at ``target/rootDirName-assebly-x.x.x-SNAPSHOT.jar``.

sbt shellで``assembly``コマンドを実行すると``target/``以下にfat .jarファイルが生成される.

### upload zip file

※ここではCLIではなくGUIからアップロードする

Before uploading, zip ``target/rootDirName-assebly-x.x.x-SNAPSHOT.jar``.

アップロードする前に.jarファイルをzip圧縮する


In GCP cloud functions console,

1. create a function
  - set function name and region
  - set function trigger[default: http]
2. choose run type
  - java 11
3. upload zipped fat .jar file.
4. enable cloud build api
5. choose a bucket or create the new bucket if not exists.

エントリーポイントはパッケージ名とクラス名に対応させる. 以下のようなScala ソースがあるとき、エントリーポイントはfunctions.ScalaHelloWorldになる.

Set entrypoint as packageName.className.

With Scala source below, for example, set entrypoint as functions.ScalaHelloWorld

```scala
package functions
import com.google.cloud.functions.{HttpFunction, HttpRequest, HttpResponse}

class ScalaHelloWorld extends HttpFunction {
  override def service(httpRequest: HttpRequest, httpResponse: HttpResponse):Unit = {
    httpResponse.getWriter.write("hello world")
  }
}


```
