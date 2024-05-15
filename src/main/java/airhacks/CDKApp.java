package airhacks;

import airhacks.functionurl.boundary.ServerlessApp;

public interface CDKApp {

    static void main(String... args) {

        new ServerlessApp("hello-lambda")
                .functionName("airhacks_FunctionURL")
                .functionZip("../[LAMBDA_PROJECT]/target/function.zip")
                .build();
    }
}
