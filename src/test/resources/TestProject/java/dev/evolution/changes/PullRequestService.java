package dev.evolution.changes;

import com.google.gson.Gson;
import dev.evolution.utils.HttpUtil;

import java.util.Arrays;
import java.util.stream.Stream;

public class PullRequestService {

    public PullRequestInfo getPullRequestInfo(String prID) {
        String url = "http://10.135.102.62:7990/rest/api/1.0/projects/HX/repos/cbs/pull-requests/"+prID;
        String user = "benewu";
        String pwd = "111111";
        String res = HttpUtil.get(url,user,pwd);
        //System.out.println("getPullRequestInfo: "+res);
        Gson gson = new Gson();
        PullRequestInfo info = gson.fromJson(res,PullRequestInfo.class);
        return info;
    }

    public Commits getCommits(String prID) {
        String url = "http://10.135.102.62:7990/rest/api/1.0/projects/HX/repos/cbs/pull-requests/"+prID+"/commits";
        String user = "benewu";
        String pwd = "111111";
        String res = HttpUtil.get(url,user,pwd);
        //System.out.println("getCommits: "+res);
        Gson gson = new Gson();
        Commits commits = gson.fromJson(res,Commits.class);
        return commits;
    }

    public static class Commits {
        private Commit[] values;

        public Commits(Commit[] values) {
            this.values = values;
        }

        public Stream<Commit> getValues() {
            if(values != null) {
                return Arrays.stream(values);
            } else {
                return Stream.empty();
            }
        }

        public void setValues(Commit[] values) {
            this.values = values;
        }

        @Override
        public String toString() {
            return "Commits{" +
                    "values=" + Arrays.toString(values) +
                    '}';
        }
    }

    public static class Commit {
        private String id;
        private Commit[] parents = new Commit[]{};

        public Commit(String id) {
            this.id = id;
        }

        public Commit(String id, Commit[] parents) {
            this.id = id;
            this.parents = parents;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Commit[] getParents() {
            return parents;
        }

        public void setParents(Commit[] parents) {
            this.parents = parents;
        }

        @Override
        public String toString() {
            return "Commit{" +
                    "id='" + id + '\'' +
                    ", parents=" + Arrays.toString(parents) +
                    '}';
        }
    }

    public static class PullRequestInfo {
        private String title;
        private PRFromRef fromRef;
        private PRToRef toRef;

        public PullRequestInfo(String title, PRFromRef fromRef, PRToRef toRef) {
            this.title = title;
            this.fromRef = fromRef;
            this.toRef = toRef;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public PRFromRef getFromRef() {
            return fromRef;
        }

        public void setFromRef(PRFromRef fromRef) {
            this.fromRef = fromRef;
        }

        public PRToRef getToRef() {
            return toRef;
        }

        public void setToRef(PRToRef toRef) {
            this.toRef = toRef;
        }

        @Override
        public String toString() {
            return "PullRequestInfo{" +
                    "title='" + title + '\'' +
                    ", fromRef=" + fromRef +
                    ", toRef=" + toRef +
                    '}';
        }
    }

    public static class PRFromRef {
        private String id;
        private String displayId;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public PRFromRef(String id, String displayId) {
            this.id = id;
            this.displayId = displayId;
        }

        public String getDisplayId() {
            return displayId;
        }

        @Override
        public String toString() {
            return "PRFromRef{" +
                    "id='" + id + '\'' +
                    ", displayId='" + displayId + '\'' +
                    '}';
        }
    }

    public static class PRToRef {
        private String id;

        public PRToRef(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "PRToRef{" +
                    "id='" + id + '\'' +
                    '}';
        }
    }

}
