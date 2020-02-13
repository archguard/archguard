package common;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

class JGitTest {


    private Repository repository;

    @BeforeEach
    void setUp() throws IOException {
        repository = new FileRepositoryBuilder()
                .findGitDir(new File("/Users/ygdong/Downloads/gittest"))
                .build();
    }




    /*最终采用这种方式*/
    @Test
    void diffFormat() throws GitAPIException, IOException {
        try (Git git = new Git(repository)) {
            DiffFormatter formatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
            formatter.setRepository(repository);
            formatter.setDiffComparator(RawTextComparator.DEFAULT);
            formatter.setDetectRenames(true);
            int count = 0;
            for (RevCommit commit : git.log().call()) {
                count++;
                System.out.println("commit:" + commit.getName() + ", message:" + commit.getFullMessage());
                RevCommit parent = commit.getParentCount() != 0 ? commit.getParent(0) : null;
                List<DiffEntry> diffEntries = formatter.scan(parent == null ? null : parent.getTree(), commit.getTree());
                diffEntries.forEach(e ->
                        System.out.println("changeType:=" + e.getChangeType().name() + ",newID=" + e.getNewId() + ",newPath=" + e.getNewPath()));
            }
            System.out.println("count = " + count);
        }
    }


    /*这是第二种方式，也可以*/
    @Test
    void canonicalTreeParserTest() throws GitAPIException, IOException {
        try (Git git = new Git(repository)) {
            try (ObjectReader reader = repository.newObjectReader()) {
                CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
                CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
                for (RevCommit commit : git.log().call()) {
                    System.out.println("commit:" + commit.getName() + ", message:" + commit.getFullMessage());
                    RevCommit parent = commit.getParentCount() != 0 ? commit.getParent(0) : null;
                    if (parent != null)
                        oldTreeIter.reset(reader, parent.getTree());
                    newTreeIter.reset(reader, commit.getTree());
                    List<DiffEntry> diffEntries = git.diff().setNewTree(newTreeIter).setOldTree(oldTreeIter).call();
                    diffEntries.forEach(e ->
                            System.out.println("changeType:=" + e.getChangeType().name() + ",newID=" + e.getNewId() + ",newPath=" + e.getNewPath()));
                }
            }
        }
    }

    /*not work，因为会包含一个所有的文件，不仅仅是变更的文件*/
    @Test
    void testTreeWalk() throws GitAPIException, IOException {
        try (Git git = new Git(repository); TreeWalk treeWalk = new TreeWalk(repository)) {
            Iterable<RevCommit> revCommits = git.log().call();
            for (RevCommit commit : revCommits) {
                treeWalk.reset(commit.getTree());
                System.out.println("commit:" + commit.getName() + ", message:" + commit.getFullMessage());
                while (treeWalk.next()) {
                    FileMode fileMode = treeWalk.getFileMode();
                    System.out.println("fileMode = " + fileMode.toString());
                    String pathString = treeWalk.getPathString();
                    System.out.println("pathString = " + pathString);

                }
            }
        }
    }
}
