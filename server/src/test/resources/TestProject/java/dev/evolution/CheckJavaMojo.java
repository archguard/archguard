package dev.evolution;

import dev.evolution.changes.FileChanges;
import dev.evolution.changes.PullRequestService;
import dev.evolution.complexity.RecognizeComplexityCheckResult;
import dev.evolution.complexity.RecognizeComplexityService;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mojo(name = "check")
public class CheckJavaMojo extends AbstractMojo {

    private RecognizeComplexityService recognizeComplexityService = new RecognizeComplexityService();
    private PullRequestService pullRequestService = new PullRequestService();

    @Parameter(property = "dir", defaultValue = "/Users/xfwu/Workspace/PICC/2019/cbs/")
    private String dir;

    @Parameter(property = "prid", defaultValue = "17618")
    private String prid;

    public void execute() throws MojoExecutionException {
        MavenProject mavenProject = (MavenProject) getPluginContext().get("project");
        if (mavenProject.isExecutionRoot() && isWorkingDay()) {


            System.out.println("project: " + getPluginContext().get("project").getClass().getCanonicalName());
            System.out.println("dir:" + dir + ",PR ID:" + prid);
            List<RecognizeComplexityCheckResult> resList = new ArrayList<>();
            try {
                Stream<PullRequestService.Commit> commits = pullRequestService.getCommits(prid).getValues().limit(3);
                String revstr = "refs/remotes/origin/" + pullRequestService.getPullRequestInfo(prid).getFromRef().getDisplayId();
                Stream<FileChanges.Change> changes = new FileChanges(dir).javaChanges(revstr, commits.collect(Collectors.toSet())).limit(20);
                resList = changes.map(c -> recognizeComplexityService.check(c.getClz(), c.getCode())).filter(r -> !r.isOk()).collect(Collectors.toList());

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!resList.isEmpty()) {
                throw new MojoExecutionException(resList.stream().map(r -> r.toString()).reduce((acc, r) -> acc + "\n" + r).get());
            }
        }
    }

    private boolean isWorkingDay() {
        Calendar cal = Calendar.getInstance();
        int todayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return 1 == todayOfWeek || 2 == todayOfWeek || 3 == todayOfWeek || 4 == todayOfWeek;
    }
    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getPrid() {
        return prid;
    }

    public void setPrid(String prid) {
        this.prid = prid;
    }
}