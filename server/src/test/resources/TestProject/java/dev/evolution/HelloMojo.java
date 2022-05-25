package dev.evolution;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "hello")
public class HelloMojo extends AbstractMojo {

    @Parameter(property = "msg",defaultValue = "from maven")
    private String msg;
    public void execute() throws MojoExecutionException {
        getLog().info("Hello " + msg);
        getLog().info("Hello " + msg);
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

}