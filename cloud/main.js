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
    
// Parse.Cloud.define("match", function(request, response) {
//     var memberQuery = request.user.relation("memberOf").query();
//     var members = {};
//     memberQuery.find({
//         success: function(results) {
//             for (var group = 0; group < results.length; group++) {
//                 members[results[group].id] = true;
//             }
//             var query = new Parse.Query("Group");
//             query.equalTo("type", request.params.type);
//             query.descending("createdAt");
//             // query.doesNotMatchQuery("self", memberGroupQuery);
//             query.find({
//                 success: function(results) {
//                     if (request.params.type == "Hacker") {
//                         var userSkills = request.user.get("hacker_skills");
//                     } else {
//                         var userSkills = request.user.get("athlete_skills");
//                     }
//                     var userSkillSet = {};
//                     for (var index = 0; index < userSkills.length; index++) {
//                         if (userSkills[index].isSelected) {
//                             userSkillSet[userSkills[index].skill_name] = true;
//                         }
//                     }
//                     var returnList = new Array();
//                     for (var i = 0; i < results.length; i++) {
//                         if (results[i].id in members) {
//                             continue;
//                         }
//                         var skills = results[i].get("skills");
//                         var satisfies = true;
//                         if (skills !== "") {
//                             var skillArray = skills.split(", ");
//                             for (var j = 0; j < skillArray.length; j++) {
//                                 if (!(skillArray[j] in userSkillSet)) {
//                                     satisfies = false;
//                                 }
//                             }
//                         }
//                         if (satisfies) {
//                             returnList.push(results[i]);
//                         }
//                     }
//                     response.success(returnList);
//                 },
//                 error: function() {
//                     response.error("group fetch failed");
//                 }
//             });
//         },
//         error: function() {
//             response.error("memberOf group fetch failed");
//         }
//     });
// });
 
// computes the ratio: # of group's skills that are in skillSet over total # of group's skills
function countMatchingSkills(group, skillSet) {
    var score = 0;
    var groupSkillString = group.get("skills");
    if (groupSkillString) {
        var groupSkills = groupSkillString.replace(/\s*,\s*/g,',').split(",");
        for (var i = 0; i < groupSkills.length; i++) {
            if (groupSkills[i] in skillSet)
                score++;
        }
        return score / groupSkills.length;
    } else {
        return 0;
    }
}
 
// tag has to be an exact match to contribute to the score
// TODO(yjchoi): maybe compute string similarity?
function countMatchingTags(group, tagSet) {
    var score = 0;
    var groupTagString = group.get("tags");
    if (groupTagString) {
        var groupTags = groupTagString.replace(/\s*,\s*/g,',').replace(/\s+/g,',').split(/[\s,]/);   // Array of group's tags
        for (var i = 0; i < groupTags.length; i++) {
            if (groupTags[i] in tagSet)
                score++;
        }
    }
    return score;
}
 
function compareBySkills(a, b, skillSet) {
    return countMatchingSkills(b, skillSet) - countMatchingSkills(a, skillSet);
}
 
// compare function to sort by descending order of # of matching tags
function compareByTags (a, b, tagSet, skillSet) {
    var countA = countMatchingTags(a, tagSet);
    var countB = countMatchingTags(b, tagSet);
    if (countA === countB) {
        return compareBySkills(a, b, skillSet);
    }
    return countB - countA;
}
 
Parse.Cloud.define("match", function(request, response) {
    var memberQuery = request.user.relation("memberOf").query();
    var query = new Parse.Query("Group");
    query.equalTo("type", request.params.type);
    query.doesNotMatchKeyInQuery("objectId", "objectId", memberQuery);
    query.descending("createdAt");
    query.find({
        success: function(results) {
            var requestedTagSet = {};
            if (request.params.tags) {
                var requestedTags = request.params.tags.replace(/\s*,\s*/g,',').replace(/\s+/g,',').split(",");
                for (var i = 0; i < requestedTags.length; i++) {
                    requestedTagSet[requestedTags[i]] = true;
                }
            }
            if (request.params.type == "Hacker") {
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
            var returnList = results.sort(function(a,b) {
                return compareByTags(a, b, requestedTagSet, userSkillSet);
            });
            response.success(returnList.slice(0,5));    // return the TOP 5 best-matching groups
        },
        error: function() {
            response.error("Group fetch failed");
        }
    });
});
    
Parse.Cloud.define("matchGroupName", function(request, response) {
    var query = new Parse.Query("Group");
    query.equalTo("name", request.params.name);
    query.include("owner, members");
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
    var tagString = group.get("tags").replace(/\s*,\s*/g,',');  // remove whitespaces surrounding comma
    group.set("tags", tagString);
    var keywords = group.get("name").split(' ');
    var hashtags = tagString.split(',');
    keywords = _.map(keywords, toLowerCase);
    hashtags = _.map(hashtags, toLowerCase);
    var stopWords = ["the", "in", "and", "to", "but", "for", "or", "yet", "so"];
       
    Array.prototype.contains = function ( needle ) {
    for (i in this) {
       if (this[i] == needle) return true;
    }
    return false;
    }
       
    keywords = keywords.filter(function (w) {
    return w.match(/[a-zA-Z0-9]+/) && !stopWords.contains(w);
    });
 
    hashtags = hashtags.filter(function (w) {
    return w.match(/[a-zA-Z0-9]+/) && !stopWords.contains(w);
    });
   
 //    var hashtags = keywords;
    // hashtags = hashtags.filter(function (w) {
 //    return w.indexOf('#') > -1;
 //    });
   
    group.set("keywords", keywords);
    group.set("hashtags", hashtags);
    response.success();
});
  
Parse.Cloud.define("partialStringSearch", function(request, response) {
    var toLowerCase = function(w) { return w.toLowerCase(); };
    var removeNewline = function(w) {return w.replace(/(\r\n|\n|\r)/gm,"");};
	
    var keywords = request.params.name.split(' ');
    keywords = _.map(keywords, toLowerCase);
	keywords = _.map(keywords, removeNewline);
    var stopWords = ["the", "in", "and", "to", "but", "for", "or", "yet", "so"];
       
    Array.prototype.contains = function ( needle ) {
    for (i in this) {
       if (this[i] == needle) return true;
    }
    return false;
    }
       
    keywords = keywords.filter(function (w) {
    return w.match(/[a-zA-Z0-9]+/) && !stopWords.contains(w);
    });
       
    var query = new Parse.Query("Group");
    query.containsAll("keywords", keywords);
    query.include("owner, members");
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
 
Parse.Cloud.define("customTagsSearch", function(request, response) {
    var toLowerCase = function(w) { return w.toLowerCase(); };
	var removeNewline = function(w) {return w.replace(/(\r\n|\n|\r)/gm,"");};
   
    var keywords = request.params.tags.split(',');
    keywords = _.map(keywords, toLowerCase);
	keywords = _.map(keywords, removeNewline);
    var stopWords = ["the", "in", "and", "to", "but", "for", "or", "yet", "so"];
      
    Array.prototype.contains = function ( needle ) {
    for (i in this) {
       if (this[i] == needle) return true;
    }
    return false;
    }
      
    keywords = keywords.filter(function (w) {
    return w.match(/[a-zA-Z0-9]+/) && !stopWords.contains(w);
    });
      
    var query = new Parse.Query("Group");
    query.containsAll("hashtags", keywords);
    query.include("owner, members");
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