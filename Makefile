.PHONY: clean aar

aar:
	cd Sources/ && ./gradlew clean
	cd Sources/ && ./gradlew :sdk:assembleRelease --no-build-cache
	cp Sources/sdk/build/outputs/aar/ONS-release.aar public-sdk/ONS.aar
	cp Sources/sdk/build/outputs/mapping/release/mapping.txt public-sdk/ONS.mapping.txt
	sh ./copy_release_mapping.sh

clean:
	rm -f public-sdk/ONS.aar
	cd Sources && ./gradlew clean