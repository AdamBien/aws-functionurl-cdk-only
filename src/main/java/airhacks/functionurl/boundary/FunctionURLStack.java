package airhacks.functionurl.boundary;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

import airhacks.lambda.control.FunctionURL;
import airhacks.lambda.control.QuarkusLambda;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.Tags;
import software.constructs.Construct;

public class FunctionURLStack extends Stack {

    public static class Builder {

        Construct construct;
        String stackId;
        String functionName;
        String functionHandler = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest";;
        Map<String, String> configuration = Map.of();
        String functionZipLocation;
        final int ONE_CPU = 1700;
        int ram = ONE_CPU;

        public Builder(Construct construct, String stackNamePrefix) {
            this.construct = construct;
            this.stackId = stackNamePrefix.toLowerCase() + "-function-url-stack";
        }

        public Builder functionName(String functionName) {
            this.functionName = functionName;
            return this;
        }

        public Builder functionHandler(String handler) {
            this.functionHandler = handler;
            return this;
        }

        public Builder ram(int ram) {
            this.ram = ram;
            return this;
        }

        public Builder withOneCPU() {
            this.ram = ONE_CPU;
            return this;
        }

        public Builder withHalfCPU() {
            this.ram = ONE_CPU / 2;
            return this;
        }

        public Builder withTwoCPUs() {
            this.ram = ONE_CPU * 2;
            return this;
        }

        /**
         * 
         * @param location the full path to the function.zip archive.
         * @return
         */
        public Builder functionZip(String location) {
            verifyFunctionZip(location);
            this.functionZipLocation = location;
            return this;
        }

        public Builder quarkusLambdaProjectLocation(String location) {
            this.functionZipLocation = location + "/target/function.zip";
            return this;
        }

        public Builder configuration(Map<String, String> configuration) {
            this.configuration = configuration;
            return this;
        }

        public FunctionURLStack build() {
            Objects.requireNonNull(this.functionName, "Function name is required");
            return new FunctionURLStack(this);
        }

        static boolean verifyFunctionZip(String functionZipFile) {
            if (!functionZipFile.endsWith("function.zip")) {
                throw new IllegalArgumentException("File must end with function.zip, but was: " + functionZipFile);
            }
            var exists = Files.exists(Path.of(functionZipFile));
            if (!exists) {
                throw new IllegalArgumentException("function.zip not found at: " + functionZipFile);
            }
            return true;
        }

    }

    public FunctionURLStack(Builder builder) {
        super(builder.construct, builder.stackId);
        var quarkusLambda = new QuarkusLambda(this, builder.functionZipLocation, builder.functionName,
                builder.functionHandler, builder.ram,
                builder.configuration);
        var function = quarkusLambda.getFunction();
        var functionURL = FunctionURL.expose(function);
        addTags(builder.stackId);
        CfnOutput.Builder.create(this, "FunctionURLOutput").value(functionURL.getUrl()).build();
    }

    void addTags(String appName) {
        Tags.of(this).add("application", appName);
        Tags.of(this).add("project", "AWS Lambda with FunctionURL");
        Tags.of(this).add("environment", "development");

    }
}
