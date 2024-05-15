package airhacks.functionurl.boundary;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

import software.amazon.awscdk.App;

public class StackBuilder {
    App app;
    String stackId;
    String functionName;
    String functionHandler = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest";;
    Map<String, String> configuration = Map.of();
    String functionZipLocation;
    final int ONE_CPU = 1700;
    int ram = ONE_CPU;

    public StackBuilder(App construct, String stackNamePrefix) {
        this.app = construct;
        this.stackId = stackNamePrefix.toLowerCase() + "-function-url";
    }
    public StackBuilder(String stackNamePrefix) {
        this(new App(),stackNamePrefix);
    }

    public StackBuilder functionName(String functionName) {
        this.functionName = functionName;
        return this;
    }

    public StackBuilder functionHandler(String handler) {
        this.functionHandler = handler;
        return this;
    }

    public StackBuilder ram(int ram) {
        this.ram = ram;
        return this;
    }

    public StackBuilder withOneCPU() {
        this.ram = ONE_CPU;
        return this;
    }

    public StackBuilder withHalfCPU() {
        this.ram = ONE_CPU / 2;
        return this;
    }

    public StackBuilder withTwoCPUs() {
        this.ram = ONE_CPU * 2;
        return this;
    }

    /**
     * 
     * @param location the full path to the function.zip archive.
     * @return
     */
    public StackBuilder functionZip(String location) {
        verifyFunctionZip(location);
        this.functionZipLocation = location;
        return this;
    }

    public StackBuilder quarkusLambdaProjectLocation(String location) {
        this.functionZipLocation = location + "/target/function.zip";
        return this;
    }

    public StackBuilder configuration(Map<String, String> configuration) {
        this.configuration = configuration;
        return this;
    }

    public FunctionURLStack build() {
        Objects.requireNonNull(this.functionName, "Function name is required");
        var stack = new FunctionURLStack(this);
        this.app.synth();
        return stack;
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
