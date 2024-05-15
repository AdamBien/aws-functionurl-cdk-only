package airhacks;

import airhacks.functionurl.boundary.StackBuilder;
import software.amazon.awscdk.App;

public interface CDKApp {

    static void main(String... args) {

        new StackBuilder("hello-lambda")
                .functionName("airhacks_FunctionURL")
                .functionZip("../[LAMBDA_PROJECT]/target/function.zip")
                .build();
    }
}
