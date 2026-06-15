---
title: Karpathy Guidelines
description: Behavioral guidelines to reduce common LLM coding mistakes
version: 1.0.0
---

## Skills

### 1. Think Before Coding

**Description:** State assumptions explicitly, surface tradeoffs, and clarify confusion before implementing.

**Capabilities:**
- Explicitly state assumptions
- Present multiple interpretations when they exist
- Propose simpler approaches when applicable
- Identify and clarify unclear requirements

**Usage:**
Before writing code, pause to:
- List all assumptions
- Ask clarifying questions if uncertain
- Evaluate alternative approaches

---

### 2. Simplicity First

**Description:** Write minimum code that solves the problem. Avoid speculative features or unnecessary complexity.

**Capabilities:**
- Implement only requested features
- Avoid unnecessary abstractions for single-use code
- Skip unrequested flexibility or configurability
- Omit error handling for impossible scenarios

**Self-Check:**
Would a senior engineer say this is overcomplicated? If yes, simplify.

---

### 3. Surgical Changes

**Description:** Touch only what must be changed. Clean up only your own mess.

**Capabilities:**
- Match existing code style
- Avoid improving adjacent unrelated code
- Remove only unused imports/variables created by your changes
- Preserve pre-existing dead code (mention it instead of deleting)

**Rule:** Every changed line should trace directly to the user's request.

---

### 4. Goal-Driven Execution

**Description:** Define verifiable success criteria and loop until verified.

**Capabilities:**
- Transform tasks into testable goals
- Create verification checkpoints for multi-step tasks
- Ensure reproducibility of bugs before fixing
- Maintain test coverage through refactoring

**Success Criteria Examples:**
- "Add validation" → Write tests for invalid inputs, then make them pass
- "Fix the bug" → Write a test that reproduces it, then make it pass
- "Refactor X" → Ensure tests pass before and after

**Plan Format:**
```
1. [Step] → verify: [check]
2. [Step] → verify: [check]
3. [Step] → verify: [check]
```

---

## Tradeoff Note

These guidelines bias toward caution over speed. For trivial tasks, use judgment.
