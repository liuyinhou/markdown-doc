function checkEmptyString(str) {
	if (str.length == 0
			|| str == ""
			|| str.replace(/\s/g, "").length ==0
			|| str == null) {
		return true;
	}
	return false;
}

function logout() {
	$.cookie('token', null, { path: "/"});
	$.cookie('userId', null, { path: "/"});
	$.cookie('userName', null, { path: "/"});
	window.location.href = "/auth/logout";
}