
//created by Sandra Suttiratana 
//Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

// Parse.Cloud.define("search", function(request, response) {
// 	var query = new Parse.Query("Group");
// 	query.contains("name", request.params.searchKey);

// });

// TODO(yj94choi): custom tags
Parse.Cloud.define("match", function(request, response) {
	var query = new Parse.Query("Group");
	query.notEqualTo("members", request.user);
	query.equalTo("type", request.params.type);
	query.include("owner");
	query.descending("createdAt");
	query.find({
		success: function(results) {
			if (request.params.type === "Hacker") {
				var userSkills = request.user.get("hacker_skills");
			} else {
				var userSkills = request.user.get("athlete_skills");
			}
			var userSkillSet = {};
			for (var userSkill in userSkills) {
				if (userSkill.isSelected) {
					userSkillSet[userSkill.skill_name] = true;
				}
			}
			var returnList = new Array();
			for (var i = 0; i < results.length; i++) {
				var skills = results[i].get("skills");
				var skillArray = skills.split(',');
				var satisfies = true;
				for (var skill in skillArray) {
					if (!skill in userSkillSet) {
						satisfies = false;
					}
				}
				if (satisfies) {
					returnList.push(results[i]);
				}
			}
			response.success(returnList);
		},
		error: function() {
			response.error("group fetch failed");
		}
	});
});