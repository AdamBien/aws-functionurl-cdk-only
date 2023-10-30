package airhacks;

import airhacks.functionurl.boundary.FunctionURLStack;
import software.amazon.awscdk.App;

public interface CDKApp {

    static void main(String... args) {

        var app = new App();
        var appName = "functionurl-lambda";

        new FunctionURLStack.Builder(app, appName)
                .functionName("airhacks_FunctionURL")
                .functionZip("../[LAMBDA_PROJECT]/target/function.zip")
                .build();
        app.synth();
    }
}
