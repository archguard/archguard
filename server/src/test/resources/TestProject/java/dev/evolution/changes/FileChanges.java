package dev.evolution.changes;

import dev.evolution.utils.FileUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class FileChanges {

    private String dir;
    public FileChanges(String dir) {
        this.dir = dir;
    }

    public Stream<Change> javaChanges(String revstr, Set<PullRequestService.Commit> commits) throws IOException, GitAPIException {
        System.out.println("FileChanges.javaChanges, revstr="+revstr+", commitIDs size " + commits.size());
        // Open an existing repository
        try(Repository repo = new FileRepositoryBuilder()
                .findGitDir(new File(dir))
                .build()) {
            List<Ref> refs = repo.getRefDatabase().getRefs();
            //refs.forEach(f -> System.out.println(f.getName()));
            //System.out.println(repo.getBranch());
            // Get a reference
            try(Git git = new Git(repo)){
                return gitChanges(repo,git,revstr,commits).stream().filter(f -> f.endsWith(".java") && !f.contains("test")).map(f -> new Change(f,convertToClz(f)));
            }
        }
    }

    private String convertToClz(String file) {
        System.out.println("FileChanges.convertToClz: "+file);
        return file.split("src")[1].substring("/main/java/".length()).replace("/",".").replace(".java","");
    }
    private Set<String> gitChanges(Repository repo, Git git, String revstr, Set<PullRequestService.Commit> commitList) throws GitAPIException, IOException {
        Set<String> changes = new HashSet<>();
        Iterable<RevCommit> logs = git.log().add(repo.resolve(revstr)).call();
        List<RevCommit> commits = new ArrayList<>();
        for (RevCommit rev : logs) {
            commits.add(rev);
        }

        for(PullRequestService.Commit commit:commitList){
            commits.stream().filter( c -> commit.getId().endsWith(c.getId().getName())).forEach(c -> System.out.println(c.getFullMessage()));
            List<String> _changes = listDiff(repo,git, commit.getId(), commit.getParents()[0].getId());
            changes.addAll(_changes);
        }
        return changes;
    }

    private List<String> showDiff(Repository repo, Git git, List<RevCommit> commits, int index) throws GitAPIException, IOException {
        System.out.println(commits.get(index).getFullMessage());
        return listDiff(repo,git,commits.get(index+1).getId().getName(),commits.get(index).getId().getName());
    }

    private List<String> listDiff(Repository repository, Git git, String oldCommit, String newCommit) throws GitAPIException, IOException {
        final List<DiffEntry> diffs = git.diff()
                .setOldTree(prepareTreeParser(repository, oldCommit))
                .setNewTree(prepareTreeParser(repository, newCommit))
                .call();

        System.out.println("Found: " + diffs.size() + " differences");
        List<String> changes = new ArrayList<>();
        for (DiffEntry diff : diffs) {
            System.out.println("Diff: " + diff.getChangeType() + ": " +
                    (diff.getOldPath().equals(diff.getNewPath()) ? diff.getNewPath() : diff.getOldPath() + " -> " + diff.getNewPath()));
            if(DiffEntry.ChangeType.MODIFY.equals(diff.getChangeType())){
                changes.add(dir+"/"+diff.getNewPath());
            }
        }
        return changes;
    }

    private AbstractTreeIterator prepareTreeParser(Repository repository, String objectId) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        //noinspection Duplicates
        try {
            try (RevWalk walk = new RevWalk(repository)) {
                RevCommit commit = walk.parseCommit(repository.resolve(objectId));
                RevTree tree = walk.parseTree(commit.getTree().getId());

                CanonicalTreeParser treeParser = new CanonicalTreeParser();
                try (ObjectReader reader = repository.newObjectReader()) {
                    treeParser.reset(reader, tree.getId());
                }

                walk.dispose();

                return treeParser;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CanonicalTreeParser();
        }
    }

    public static class Change {
        private String file;
        private String clz;

        public Change(String file, String clz) {
            this.file = file;
            this.clz = clz;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getClz() {
            return clz;
        }

        public void setClz(String clz) {
            this.clz = clz;
        }

        public String getCode() {
            String file = getFile();
            return FileUtil.read(file);
        }

        @Override
        public String toString() {
            return "Change{" +
                    "file='" + file + '\'' +
                    ", clz='" + clz + '\'' +
                    '}';
        }
    }

}
