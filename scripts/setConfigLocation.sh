# This script is only needed in a development environment for setting the environment
# variable that has the location of the test configuration directory for your console.
# Usage scenarios:  before running mvn, or in tomcat startup script
#
# This is the unix version of the script.  It has been tested with bash.
#
# Optionally, you can simply set the environment variable below in your global
# environment instead of having to run it in each console.

export SSP_CONFIGDIR=/usr/local/etc/ssp
export SSP_TESTCONFIGDIR=/usr/local/etc/ssp

# Uncomment the following to allow SSP to run in absence of uportal in the dev environment
#export spring.profiles.active=dev-standalone

echo The configuration file for SSP should be in:
echo $SSP_TESTCONFIGDIR for the TEST environment, and $SSP_CONFIGDIR for the DEPLOY environment
echo Please verify the file exists and is populated with the correct values.
