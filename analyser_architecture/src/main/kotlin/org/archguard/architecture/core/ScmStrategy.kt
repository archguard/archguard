package org.archguard.architecture.core

/**
 * The `ScmStrategy` enum class is used to define the SCM strategy to be used in a project. It provides a way to specify the
 * version control workflow that should be followed when managing the source code.
 *
 * The `ScmStrategy` enum class has a single value, `GitFlow`, which represents the GitFlow SCM strategy. GitFlow is a branching
 * model for Git that helps teams manage their source code by defining a set of rules for creating, merging, and releasing
 * branches. It provides a structured approach to version control and facilitates collaboration among team members.
 *
 * Note that the `ScmStrategy` enum class can be extended in the future to support additional SCM strategies if needed.
 *
 * @see [GitFlow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow)
 */
enum class ScmStrategy {
    GitFlow,
    OneFlow,
    TrunkBasedDevelopment,
    FeatureBranchWorkflow,
    GitHubFlow,
    GitLabFlow,
    BitbucketFlow,
    Custom
}