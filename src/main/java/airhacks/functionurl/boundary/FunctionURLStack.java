package airhacks.functionurl.boundary;

import java.net.http.HttpClient;
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
        boolean snapStart = false;
        String functionName;
        String functionHandler = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest";;
        Map<String, String> configuration = Map.of();
        String functionZipLocation;

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

        /**
         * 
         * @param location the full path to the function.zip archive.
         * @return
         */
        public Builder functionZip(String location) {
            this.functionZipLocation = location;
            return this;
        }

        public Builder snapStart(boolean snapStart) {
            this.snapStart = snapStart;
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

    }

    public FunctionURLStack(Builder builder) {
        super(builder.construct, builder.stackId);
        var quarkusLambda = new QuarkusLambda(this, builder.functionName, builder.functionHandler, builder.snapStart,
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