package airhacks;

import airhacks.functionurl.boundary.FunctionURLStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Tags;



public interface CDKApp {

    static void main(String... args) {

            var app = new App();
            var appName = "functionurl-lambda";

            Tags.of(app).add("project", "AWS Lambda with FunctionURL");
            Tags.of(app).add("environment","development");
            Tags.of(app).add("application", appName);

            var snapStart = false;
            new FunctionURLStack(app,appName,snapStart);
            app.synth();
        }
}
