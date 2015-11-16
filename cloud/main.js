//created by Sandra Suttiratana 
//Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});
 
// Parse.Cloud.define("search", function(request, response) {
//  var query = new Parse.Query("Group");
//  query.contains("name", request.params.searchKey);
 
// });
 
// TODO(yj94choi): custom tags
Parse.Cloud.define("match", function(request, response) {
    var query = new Parse.Query("Group");
    query.notEqualTo("members", request.user);
    query.equalTo("type", request.params.type);
    query.descending("createdAt");
    query.include("owner, members");
    query.find({
        success: function(results) {
            if (request.params.type === "Hacker") {
                var userSkills = request.user.get("hacker_skills");
            } else {
                var userSkills = request.user.get("athlete_skills");
            }
            var userSkillSet = {};
            for (var index = 0; index < userSkills.length; index++) {
                if (userSkills[index].isSelected) {
                    userSkillSet[userSkills[index].skill_name] = true;
                }
            }
            var returnList = new Array();
            for (var i = 0; i < results.length; i++) {
                var skills = results[i].get("skills");
                var skillArray = skills.split(',');
                console.log(skillArray);
                var satisfies = true;
                for (var j = 0; j < skillArray.length; j++) {
                    if (!(skillArray[j] in userSkillSet)) {
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
 
Parse.Cloud.define("matchGroupName", function(request, response) {
    var query = new Parse.Query("Group");
    query.equalTo("name", request.params.name);
    query.find({
        success: function(results) {
            // just return the results for now
            response.success(results);
        },
        error: function() {
            response.error("group fetch failed");
        }
    });
});
 
 
var _ = require("underscore");
Parse.Cloud.beforeSave("Group", function(request, response) {
    var group = request.object;
 
    var toLowerCase = function(w) { return w.toLowerCase(); };
 
    var keywords = group.get("name").split(/b/);
    keywords = _.map(keywords, toLowerCase);
    var stopWords = ["the", "in", "and"]
    words = _.filter(keywords, function(w) { return w.match(/^w+$/) && ! _.contains(stopWords, w); });
 
    var hashtags = group.get("name").match(/#.+?b/g);
    hashtags = _.map(hashtags, toLowerCase);
 
    group.set("keywords", keywords);
    group.set("hashtags", hashtags);
    response.success();
});