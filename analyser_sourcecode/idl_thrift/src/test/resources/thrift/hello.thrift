namespace php BlogService

service BlogService {
  string createPost(1:string title, 2:string content);
  bool deletePost(1:i64 postId);
  string updatePost(1:i64 postId, 2:string title, 3:string content);
  string getPost(1:i64 postId);
}
