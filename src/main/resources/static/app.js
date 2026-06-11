const state = {
    authMode: "login",
    user: null,
    controlWorks: [],
    selectedWork: null,
    selectedGroup: null,
    selectedStudent: null,
    groups: [],
    students: [],
    assignments: []
};

const commentLabels = {
    NO_COMMENT: "Без комментария",
    TASK_NOT_COMPLETED: "Задание не выполнено",
    COMPLETED_CORRECTLY: "Выполнено корректно"
};

const checkStatusLabels = {
    NOT_CHECKED: "Не проверено",
    IN_PROGRESS: "В процессе",
    CHECKED: "Проверено",
    MISSING_WORK: "Нет работы"
};

const passingStatusLabels = {
    NOT_CHECKED: "Не готово",
    PASSED: "Прошел",
    FAILED: "Не прошел"
};

const elements = {};

document.addEventListener("DOMContentLoaded", () => {
    bindElements();
    bindEvents();
    restoreUser();
    renderAuthMode();
    renderLayout();
});

function bindElements() {
    const ids = [
        "authView", "workspaceView", "currentUser", "logoutButton", "loginTab", "registerTab", "authForm",
        "authUsername", "authPassword", "authSubmit", "refreshWorksButton", "controlWorkList",
        "createWorkForm", "workTitle", "passingScore", "importForm", "studentListPath", "variantsPath",
        "selectedWorkTitle", "passingScoreForm", "newPassingScore", "groupsCount", "groupList",
        "studentList", "studentTitle", "studentMeta", "assignmentList", "missingWorkButton",
        "reportButton", "reportPanel", "reportMeta", "reportTable", "closeReportButton", "toast"
    ];
    for (const id of ids) {
        elements[id] = document.getElementById(id);
    }
}

function bindEvents() {
    elements.loginTab.addEventListener("click", () => switchAuthMode("login"));
    elements.registerTab.addEventListener("click", () => switchAuthMode("register"));
    elements.authForm.addEventListener("submit", submitAuth);
    elements.logoutButton.addEventListener("click", logout);
    elements.refreshWorksButton.addEventListener("click", loadControlWorks);
    elements.createWorkForm.addEventListener("submit", createControlWork);
    elements.importForm.addEventListener("submit", importControlWork);
    elements.passingScoreForm.addEventListener("submit", updatePassingScore);
    elements.missingWorkButton.addEventListener("click", markMissingWork);
    elements.reportButton.addEventListener("click", loadReport);
    elements.closeReportButton.addEventListener("click", () => elements.reportPanel.classList.add("hidden"));
}

function restoreUser() {
    const savedUser = localStorage.getItem("statisticsCheckerUser");
    if (!savedUser) {
        return;
    }
    try {
        state.user = JSON.parse(savedUser);
        loadControlWorks();
    } catch {
        localStorage.removeItem("statisticsCheckerUser");
    }
}

function switchAuthMode(mode) {
    state.authMode = mode;
    renderAuthMode();
}

function renderAuthMode() {
    const isLogin = state.authMode === "login";
    elements.loginTab.classList.toggle("active", isLogin);
    elements.registerTab.classList.toggle("active", !isLogin);
    elements.authSubmit.textContent = isLogin ? "Войти" : "Зарегистрироваться";
}

function renderLayout() {
    const isLoggedIn = Boolean(state.user);
    elements.authView.classList.toggle("hidden", isLoggedIn);
    elements.workspaceView.classList.toggle("hidden", !isLoggedIn);
    elements.currentUser.classList.toggle("hidden", !isLoggedIn);
    elements.logoutButton.classList.toggle("hidden", !isLoggedIn);
    if (state.user) {
        elements.currentUser.textContent = state.user.username;
    }
}

async function submitAuth(event) {
    event.preventDefault();
    const username = elements.authUsername.value.trim();
    const password = elements.authPassword.value;
    const path = state.authMode === "login" ? "/api/auth/login" : "/api/auth/register";
    const user = await request(path, {
        method: "POST",
        body: { username, password }
    });
    if (!user) {
        return;
    }
    state.user = user;
    localStorage.setItem("statisticsCheckerUser", JSON.stringify(user));
    elements.authPassword.value = "";
    renderLayout();
    await loadControlWorks();
    showToast("Вход выполнен");
}

function logout() {
    localStorage.removeItem("statisticsCheckerUser");
    state.user = null;
    state.controlWorks = [];
    state.selectedWork = null;
    state.selectedGroup = null;
    state.selectedStudent = null;
    renderLayout();
    renderControlWorks();
    renderGroups();
    renderStudents();
    renderAssignments();
}

async function loadControlWorks() {
    if (!state.user) {
        return;
    }
    const works = await request(`/api/control-works?createdByUserId=${state.user.id}`);
    if (!works) {
        return;
    }
    state.controlWorks = works;
    if (state.selectedWork) {
        state.selectedWork = works.find((work) => work.id === state.selectedWork.id) || null;
    }
    if (!state.selectedWork && works.length > 0) {
        state.selectedWork = works[0];
        await loadGroups();
    }
    renderControlWorks();
    renderSelectedWork();
}

async function createControlWork(event) {
    event.preventDefault();
    if (!state.user) {
        return;
    }
    const title = elements.workTitle.value.trim();
    const passingScore = elements.passingScore.value;
    const work = await request("/api/control-works", {
        method: "POST",
        body: {
            createdByUserId: state.user.id,
            title,
            passingScore
        }
    });
    if (!work) {
        return;
    }
    elements.createWorkForm.reset();
    state.selectedWork = work;
    await loadControlWorks();
    await loadGroups();
    showToast("Контрольная работа создана");
}

async function selectControlWork(workId) {
    state.selectedWork = state.controlWorks.find((work) => work.id === workId);
    state.selectedGroup = null;
    state.selectedStudent = null;
    state.groups = [];
    state.students = [];
    state.assignments = [];
    elements.reportPanel.classList.add("hidden");
    renderControlWorks();
    renderSelectedWork();
    await loadGroups();
}

async function updatePassingScore(event) {
    event.preventDefault();
    if (!state.selectedWork) {
        showToast("Выберите контрольную работу", true);
        return;
    }
    const passingScore = elements.newPassingScore.value;
    const work = await request(`/api/control-works/${state.selectedWork.id}/passing-score`, {
        method: "PATCH",
        body: { passingScore }
    });
    if (!work) {
        return;
    }
    state.selectedWork = work;
    await loadControlWorks();
    if (state.selectedStudent) {
        await loadAssignments(state.selectedStudent.id);
    }
    showToast("Проходной балл обновлен");
}

async function importControlWork(event) {
    event.preventDefault();
    if (!state.selectedWork) {
        showToast("Выберите контрольную работу", true);
        return;
    }
    const studentListFilePath = elements.studentListPath.value.trim();
    const variantsDirectoryPath = elements.variantsPath.value.trim();
    const result = await request(`/api/control-works/${state.selectedWork.id}/import`, {
        method: "POST",
        body: { studentListFilePath, variantsDirectoryPath },
        empty: true
    });
    if (result === null) {
        return;
    }
    await loadControlWorks();
    await loadGroups();
    showToast("Импорт выполнен");
}

async function loadGroups() {
    if (!state.selectedWork) {
        renderGroups();
        return;
    }
    const groups = await request(`/api/control-works/${state.selectedWork.id}/groups`);
    if (!groups) {
        return;
    }
    state.groups = groups;
    if (!state.selectedGroup && groups.length > 0) {
        state.selectedGroup = groups[0];
        await loadStudents();
    }
    renderGroups();
}

async function selectGroup(groupId) {
    state.selectedGroup = state.groups.find((group) => group.id === groupId);
    state.selectedStudent = null;
    state.students = [];
    state.assignments = [];
    elements.reportPanel.classList.add("hidden");
    renderGroups();
    renderStudents();
    renderAssignments();
    await loadStudents();
}

async function loadStudents() {
    if (!state.selectedGroup) {
        renderStudents();
        return;
    }
    const students = await request(`/api/groups/${state.selectedGroup.id}/students`);
    if (!students) {
        return;
    }
    state.students = students;
    renderStudents();
}

async function selectStudent(studentId) {
    state.selectedStudent = state.students.find((student) => student.id === studentId);
    elements.reportPanel.classList.add("hidden");
    renderStudents();
    await loadAssignments(studentId);
}

async function loadAssignments(studentId) {
    const assignments = await request(`/api/students/${studentId}/assignments`);
    if (!assignments) {
        return;
    }
    state.assignments = assignments;
    renderAssignments();
}

async function saveGrade(assignmentId) {
    if (!state.selectedStudent) {
        return;
    }
    const scoreInput = document.querySelector(`[data-score-for="${assignmentId}"]`);
    const commentSelect = document.querySelector(`[data-comment-for="${assignmentId}"]`);
    const result = await request(`/api/students/${state.selectedStudent.id}/assignments/${assignmentId}/grade`, {
        method: "PUT",
        body: {
            score: scoreInput.value,
            commentTemplate: commentSelect.value
        }
    });
    if (!result) {
        return;
    }
    await reloadSelectedStudent();
    await loadAssignments(state.selectedStudent.id);
    showToast("Оценка сохранена");
}

async function deleteGrade(assignmentId) {
    if (!state.selectedStudent) {
        return;
    }
    const result = await request(`/api/students/${state.selectedStudent.id}/assignments/${assignmentId}/grade`, {
        method: "DELETE",
        empty: true
    });
    if (result === null) {
        return;
    }
    await reloadSelectedStudent();
    await loadAssignments(state.selectedStudent.id);
    showToast("Оценка удалена");
}

async function markMissingWork() {
    if (!state.selectedStudent) {
        showToast("Выберите студента", true);
        return;
    }
    const result = await request(`/api/students/${state.selectedStudent.id}/missing-work`, {
        method: "POST"
    });
    if (!result) {
        return;
    }
    await reloadSelectedStudent();
    await loadAssignments(state.selectedStudent.id);
    showToast("Работа отмечена как отсутствующая");
}

async function reloadSelectedStudent() {
    if (!state.selectedStudent) {
        return;
    }
    const student = await request(`/api/students/${state.selectedStudent.id}`);
    if (!student) {
        return;
    }
    state.selectedStudent = student;
    state.students = state.students.map((item) => item.id === student.id ? student : item);
    renderStudents();
}

async function loadReport() {
    if (!state.selectedWork || !state.selectedGroup) {
        showToast("Выберите контрольную работу и группу", true);
        return;
    }
    const report = await request(`/api/control-works/${state.selectedWork.id}/groups/${state.selectedGroup.id}/report`);
    if (!report) {
        return;
    }
    renderReport(report);
}

function renderControlWorks() {
    elements.controlWorkList.innerHTML = "";
    if (state.controlWorks.length === 0) {
        elements.controlWorkList.innerHTML = `<div class="empty-state">Создайте первую контрольную работу.</div>`;
        return;
    }
    for (const work of state.controlWorks) {
        const button = document.createElement("button");
        button.type = "button";
        button.className = `work-item ${state.selectedWork && state.selectedWork.id === work.id ? "active" : ""}`;
        button.innerHTML = `
            <span class="item-title">${escapeHtml(work.title)}</span>
            <span class="item-meta">ID ${work.id} · проходной ${formatScore(work.passingScore)}</span>
        `;
        button.addEventListener("click", () => selectControlWork(work.id));
        elements.controlWorkList.appendChild(button);
    }
}

function renderSelectedWork() {
    if (!state.selectedWork) {
        elements.selectedWorkTitle.textContent = "Выберите контрольную работу";
        elements.newPassingScore.value = "";
        return;
    }
    elements.selectedWorkTitle.textContent = state.selectedWork.title;
    elements.newPassingScore.value = state.selectedWork.passingScore;
}

function renderGroups() {
    elements.groupList.innerHTML = "";
    elements.groupsCount.textContent = state.groups.length;
    if (state.groups.length === 0) {
        elements.groupList.innerHTML = `<div class="empty-state">Группы появятся после импорта.</div>`;
        return;
    }
    for (const group of state.groups) {
        const button = document.createElement("button");
        button.type = "button";
        button.className = `list-item ${state.selectedGroup && state.selectedGroup.id === group.id ? "active" : ""}`;
        button.innerHTML = `<span class="item-title">${escapeHtml(group.name)}</span>`;
        button.addEventListener("click", () => selectGroup(group.id));
        elements.groupList.appendChild(button);
    }
}

function renderStudents() {
    elements.studentList.innerHTML = "";
    if (state.students.length === 0) {
        elements.studentList.innerHTML = `<div class="empty-state">Выберите группу.</div>`;
        return;
    }
    for (const student of state.students) {
        const button = document.createElement("button");
        button.type = "button";
        button.className = `list-item ${state.selectedStudent && state.selectedStudent.id === student.id ? "active" : ""}`;
        button.innerHTML = `
            <span class="item-title">${escapeHtml(student.fullName)}</span>
            <span class="item-meta">Вариант ${escapeHtml(student.variantCode)} · сумма ${formatScore(student.totalScore)}</span>
            ${renderStatus(student.checkStatus)}
        `;
        button.addEventListener("click", () => selectStudent(student.id));
        elements.studentList.appendChild(button);
    }
}

function renderAssignments() {
    if (!state.selectedStudent) {
        elements.studentTitle.textContent = "Задания";
        elements.studentMeta.textContent = "Студент не выбран";
        elements.assignmentList.className = "assignment-list empty-state";
        elements.assignmentList.textContent = "Выберите студента, чтобы открыть задания.";
        return;
    }
    elements.studentTitle.textContent = state.selectedStudent.fullName;
    elements.studentMeta.textContent = `Вариант ${state.selectedStudent.variantCode} · сумма ${formatScore(state.selectedStudent.totalScore)} · ${checkStatusLabels[state.selectedStudent.checkStatus] || state.selectedStudent.checkStatus}`;
    elements.assignmentList.className = "assignment-list";
    elements.assignmentList.innerHTML = "";
    if (state.assignments.length === 0) {
        elements.assignmentList.className = "assignment-list empty-state";
        elements.assignmentList.textContent = "У студента нет заданий.";
        return;
    }
    for (const assignment of state.assignments) {
        const item = document.createElement("div");
        item.className = "assignment-item";
        item.innerHTML = `
            <div>
                <div class="assignment-title">Задание ${assignment.number} · максимум ${formatScore(assignment.maxScore)}</div>
                <div class="assignment-text">${escapeHtml(assignment.text)}</div>
            </div>
            <label>
                <span>Балл</span>
                <input data-score-for="${assignment.id}" type="number" min="0" step="0.5" value="${assignment.score ?? ""}">
            </label>
            <label>
                <span>Комментарий</span>
                <select data-comment-for="${assignment.id}">
                    ${renderCommentOptions(assignment.commentTemplate)}
                </select>
            </label>
            <button class="primary-button" type="button" data-save-grade="${assignment.id}">Сохранить</button>
            <button class="ghost-button" type="button" data-delete-grade="${assignment.id}">Удалить</button>
        `;
        item.querySelector(`[data-save-grade="${assignment.id}"]`).addEventListener("click", () => saveGrade(assignment.id));
        item.querySelector(`[data-delete-grade="${assignment.id}"]`).addEventListener("click", () => deleteGrade(assignment.id));
        elements.assignmentList.appendChild(item);
    }
}

function renderCommentOptions(selectedValue) {
    return Object.entries(commentLabels)
        .map(([value, label]) => `<option value="${value}" ${value === selectedValue ? "selected" : ""}>${label}</option>`)
        .join("");
}

function renderReport(report) {
    elements.reportPanel.classList.remove("hidden");
    elements.reportMeta.textContent = `${report.controlWorkTitle} · ${report.groupName} · проходной ${formatScore(report.passingScore)}`;
    const rows = report.students.map((student) => {
        const assignments = student.assignments
            .map((assignment) => `№${assignment.number}: ${formatScore(assignment.score)} / ${formatScore(assignment.maxScore)} (${commentLabels[assignment.commentTemplate] || "Без комментария"})`)
            .join("<br>");
        return `
            <tr>
                <td>${escapeHtml(student.fullName)}</td>
                <td>${escapeHtml(student.variantCode)}</td>
                <td>${formatScore(student.totalScore)}</td>
                <td>${renderStatus(student.checkStatus)}</td>
                <td>${renderPassingStatus(student.passingStatus)}</td>
                <td>${assignments}</td>
            </tr>
        `;
    }).join("");
    elements.reportTable.innerHTML = `
        <table>
            <thead>
            <tr>
                <th>Студент</th>
                <th>Вариант</th>
                <th>Сумма</th>
                <th>Проверка</th>
                <th>Итог</th>
                <th>Задания</th>
            </tr>
            </thead>
            <tbody>${rows}</tbody>
        </table>
    `;
}

function renderStatus(status) {
    let className = "warn";
    if (status === "CHECKED") {
        className = "ok";
    }
    if (status === "MISSING_WORK") {
        className = "fail";
    }
    return `<span class="status ${className}">${checkStatusLabels[status] || status}</span>`;
}

function renderPassingStatus(status) {
    let className = "warn";
    if (status === "PASSED") {
        className = "ok";
    }
    if (status === "FAILED") {
        className = "fail";
    }
    return `<span class="status ${className}">${passingStatusLabels[status] || status}</span>`;
}

async function request(path, options = {}) {
    const config = {
        method: options.method || "GET",
        headers: {}
    };
    if (options.body !== undefined) {
        config.headers["Content-Type"] = "application/x-www-form-urlencoded;charset=UTF-8";
        config.body = new URLSearchParams(options.body).toString();
    }
    try {
        const response = await fetch(path, config);
        if (!response.ok) {
            const message = await readErrorMessage(response);
            showToast(message, true);
            return null;
        }
        if (options.empty || response.status === 204) {
            return {};
        }
        return await readJson(response);
    } catch {
        showToast("Не удалось выполнить запрос к серверу", true);
        return null;
    }
}

async function readJson(response) {
    const text = await response.text();
    if (!text) {
        return {};
    }
    return JSON.parse(text);
}

async function readErrorMessage(response) {
    const text = await response.text();
    if (!text) {
        return "Ошибка запроса";
    }
    try {
        const error = JSON.parse(text);
        if (error.message) {
            return error.message;
        }
    } catch {
        return text;
    }
    return "Ошибка запроса";
}

function showToast(message, isError = false) {
    elements.toast.textContent = message;
    elements.toast.classList.toggle("error", isError);
    elements.toast.classList.remove("hidden");
    window.clearTimeout(showToast.timer);
    showToast.timer = window.setTimeout(() => {
        elements.toast.classList.add("hidden");
    }, 3200);
}

function formatScore(value) {
    if (value === null || value === undefined || value === "") {
        return "0";
    }
    return Number(value).toString();
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}
