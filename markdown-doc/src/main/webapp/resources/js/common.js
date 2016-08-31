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
	$.cookie('token', '', { expires: -1 });
	$.cookie('userId', '', { expires: -1 });
	$.cookie('userName', '', { expires: -1 });
	window.location.href = "/auth/login";
}