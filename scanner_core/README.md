## scanner core

所有静态扫描都来自于scanner, 数据来源包括但不限于: 源文件, git, ci, logs, etc.

### roadmap

1. input analyser call graph

```json
[
  {
    "name": "language",
    "impl": "kotlin"
  },
  {
    "name": "apicalls",
    "dependencies": [
      "language"
    ]
  },
  {
    "name": "datamap",
    "dependencies": [
      "language"
    ]
  },
  {
    "name": "git"
  },
  {
    "name": "diff_changes",
    "dependencies": [
      "language",
      "git"
    ]
  }
]
```

2. sort by dependency (有向无环图)
3. execute the diagram

```
read sourcecode                 -> language[kotlin].analyse()   -> save to language.json (or put into cache[language]) 
read git logs                   -> git.analyse()                -> save to git.json

read language.json              -> api_calls.analyse()          -> save to api_calls.json
read language.json              -> datamap.analyse()            -> save to datamap.json
read language.json, git.json    -> diff_changes.analyse()       -> save to diff_changes.json
```
