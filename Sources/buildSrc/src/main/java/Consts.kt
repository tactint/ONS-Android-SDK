object ProjectConsts {
    const val COMPILE_SDK = 34

    const val KOTLIN_VERSION = "1.9.0"
    const val KOTLIN_COROUTINES_VERSION = "1.7.1"

    const val ANDROID_LINT_VERSION = "30.1.2"

    const val ANDROID_GRADLE_PLUGIN_VERSION = "8.3.0"
    const val GMS_GRADLE_PLUGIN_VERSION = "4.3.14"
}

object SDKConsts {
    const val VERSION = "2.0.3"
    const val API_LEVEL = 200
    const val MESSAGING_API_LEVEL = 12

    const val MIN_SDK = 21
    const val R_PREFIX = "com_onssdk_"
    const val NAMESPACE = "com.ons.android"
    const val TEST_NAMESPACE = "com.ons.android.test"
    const val MAVEN_GROUP_ID = "com.ons.android"
    const val MAVEN_ARTIFACT = "ons-sdk"
    const val MAVEN_ARTIFACT_VERSION = VERSION

    // SDK and Sample don't share the same deps as the SDK
    // usually wants lower versions for compatibility.
    object DependenciesVersions {
        const val ANDROIDX = "1.0.0"
    }
}

