import org.centos.pipeline.PackagePipelineUtils

/**
 * A class of methods used in the Jenkinsfile pipeline.
 * These methods are wrappers around methods in the TODO library.
 * TODO - Find out how to libraies are going to be structured
 */
class packagepipelineUtils implements Serializable {

    def packagePipelineUtils = new PackagePipelineUtils()

    // pass in from the jenkinsfile
    def cimetrics

    /**
     * Method to to find DIST_BRANCH to use for rpm NVRs
     * @return
     */
    def setDistBranch() {
        return packagePipelineUtils.setDistBranch()
    }

    /**
     * Method to set message fields to be published
     * @param messageType ${MAIN_TOPIC}.ci.pipeline.<defined-in-README>
     * @return
     */
    def setMessageFields(String messageType) {
        packagePipelineUtils.setMessageFields(messageType)
    }

    /**
     * Method to set default environmental variables. Performed once at start of Jenkinsfile
     * @param envMap Key/value pairs which will be set as environmental variables.
     * @return
     */
    def setDefaultEnvVars(Map envMap = null) {
        packagePipelineUtils.setDefaultEnvVars(envMap)
    }

    /**
     * Method to set stage specific environmental variables.
     * @param stage Current stage
     * @return
     */
    def setStageEnvVars(String stage) {
        packagePipelineUtils.setStageEnvVars(stage)
    }

    def prepareCredentials(String credentials) {
        packagePipelineUtils.prepareCredentials(credentials)
    }

    /**
     * Watch for messages
     * @param msg_provider jms-messaging message provider
     * @param message trigger message
     */
    def watchForMessages(String msg_provider, String message) {
        packagePipelineUtils.watchForMessages(msg_provider, message)
    }

    def ciPipeline(Closure body) {
        try {
            packagePipelineUtils.ciPipeline(body)
        } catch(e) {
            throw e
        } finally {
            cimetrics.writeToInflux()
        }
    }

    def handlePipelineStep(Map config, Closure body) {
        packagePipelineUtils.handlePipelineStep(config, body)
    }

    def timedPipelineStep(Map config, Closure body) {
        cimetrics.timed config.stepName, {
            packagePipelineUtils.handlePipelineStep(config, body)
        }
    }

    def packageMetrics() {
        def metrics = packagePipelineUtils.packageMetrics()
        setMetrics(metrics)

    }

    def setMetricTag(String measurement, String key, String value) {
        if (!cimetrics.customDataMapTags[measurement]) {
            cimetrics.customDataMapTags[measurement] = [:]
        }

        cimetrics.customDataMapTags[measurement][key] = value
    }

    def setMetricField(String measurement, String key, def value) {
        if (!cimetrics.customDataMap[measurement]) {
            cimetrics.customDataMap[measurement] = [:]
        }

        cimetrics.customDataMap[measurement][key] = value
    }

    def pipelineMetrics() {
        def metrics = packagePipelineUtils.pipelineMetrics()
        setMetrics(metrics)

    }

    def setMetrics(def metrics) {
        String measurement = metrics[0]
        cimetrics.customDataMapTags[measurement] = metrics[1]
        cimetrics.customDataMap[measurement] = metrics[2]

    }

    def checkBranch() {
        return packagePipelineUtils.checkBranch()
    }


}
