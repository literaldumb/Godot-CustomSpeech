def can_build(platform):
    return platform=="android"

def configure(env):
    if env['platform'] == 'android':
        # android specific code
	env.android_add_to_manifest("AndroidManifest.xml")
	env.android_add_dependency("compile files('android.jar')")
