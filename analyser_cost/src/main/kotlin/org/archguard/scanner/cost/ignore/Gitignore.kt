package org.archguard.scanner.cost.ignore

import java.io.File

// type IgnoreMatcher interface {
//	Match(path string, isDir bool) bool
//}
//
//type gitIgnore struct {
//	ignorePatterns scanStrategy
//	acceptPatterns scanStrategy
//	path           string
//}
//
//func NewGitIgnore(gitignore string, base ...string) (IgnoreMatcher, error) {
//	var path string
//	if len(base) > 0 {
//		path = base[0]
//	} else {
//		path = filepath.Dir(gitignore)
//	}
//
//	file, err := os.Open(gitignore)
//	if err != nil {
//		return nil, err
//	}
//	defer file.Close()
//
//	return NewGitIgnoreFromReader(path, file), nil
//}
//
//func NewGitIgnoreFromReader(path string, r io.Reader) gitIgnore {
//	g := gitIgnore{
//		ignorePatterns: newIndexScanPatterns(),
//		acceptPatterns: newIndexScanPatterns(),
//		path:           path,
//	}
//	scanner := bufio.NewScanner(r)
//	for scanner.Scan() {
//		line := strings.Trim(scanner.Text(), " ")
//		if len(line) == 0 || strings.HasPrefix(line, "#") {
//			continue
//		}
//
//		if strings.HasPrefix(line, "!") {
//			g.acceptPatterns.add(strings.TrimPrefix(line, "!"))
//		} else {
//			g.ignorePatterns.add(line)
//		}
//	}
//	return g
//}
//
//func (g gitIgnore) Match(path string, isDir bool) bool {
//	relativePath, err := filepath.Rel(g.path, path)
//	if err != nil {
//		return false
//	}
//
//	if g.acceptPatterns.match(relativePath, isDir) {
//		return false
//	}
//	return g.ignorePatterns.match(relativePath, isDir)
//}

interface IgnoreMatcher {
    fun match(path: String, isDir: Boolean): Boolean
}

class Gitignore(gitignore: String, base: String = "") : IgnoreMatcher {
    private val ignorePatterns: ScanStrategy = IndexScanPatterns()
    private val acceptPatterns: ScanStrategy = IndexScanPatterns()
    private val path: String

    init {
        val file = File(gitignore)
        path = if (base.isEmpty()) {
            file.parent
        } else {
            base
        }
        file.forEachLine { line ->
            val trimmedLine = line.trim()
            if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                return@forEachLine
            }
            if (trimmedLine.startsWith("!")) {
                acceptPatterns.add(trimmedLine.substring(1))
            } else {
                ignorePatterns.add(trimmedLine)
            }
        }
    }

    override fun match(path: String, isDir: Boolean): Boolean {
        val relativePath = File(path).relativeTo(File(this.path)).path
        if (acceptPatterns.match(relativePath, isDir)) {
            return false
        }
        return ignorePatterns.match(relativePath, isDir)
    }
}