package airhacks.functionurl.boundary;

import airhacks.lambda.control.FunctionURL;
import airhacks.lambda.control.QuarkusLambda;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.Tags;

public class FunctionURLStack extends Stack {


    public FunctionURLStack(StackBuilder builder) {
        super(builder.construct, builder.stackId);
        var quarkusLambda = new QuarkusLambda(this, builder.functionZipLocation, builder.functionName,
                builder.functionHandler, builder.ram,
                builder.configuration);
        var function = quarkusLambda.create();
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
