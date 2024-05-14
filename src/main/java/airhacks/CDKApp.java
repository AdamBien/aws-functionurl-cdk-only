package airhacks;

import airhacks.functionurl.boundary.StackBuilder;
import software.amazon.awscdk.App;

public interface CDKApp {

    static void main(String... args) {

        var app = new App();
        new StackBuilder(app,"hello-lambda")
                .functionName("airhacks_FunctionURL")
                .functionZip("../[LAMBDA_PROJECT]/target/function.zip")
                .build();
        app.synth();
    }
}
