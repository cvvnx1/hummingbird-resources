# How to deploy to maven center repository

## First, authentication

You need config GPG keychain in your OS, make up `secret-keys.gpg` for config file.

Then, put your authentication information into `gradle.properties`(root of project), like this:

```properties
nexusUsername=YOUR_SONATYPE_USER_NAME
nexusPassword=YOUR_SONATYPE_USER_PASSWORD
signing.keyId=KEY_ID
signing.password=KEY_PASSWORD
signing.secretKeyRingFile=/PATH/TO/SECRET/RING/FILE
```

## Second, run gradle task

Publish to maven center repository have two stage: 

- upload
- release

### upload

Execute `gradle uploadArchives` to compile and upload your release package to sonatype.org.

### release

Execute `gradle closeAndReleaseRepository` to close bundles in sonatype.org and release it. 

## Third, testing

Config `build.gradle`, use `0.0.5-SNAPSHOT` for example, add:

```text
buildscript {
    repositories {
        maven {
            name 'glide-snapshot'
            url 'http://oss.sonatype.org/content/repositories/snapshots'
        }
    }
}

dependencies {
    implementation 'com.ganwhat.hummingbird:hummingbird-resources:0.0.5-SNAPSHOT'
}

```
