var tests = require('../cloud/testModules.js');
var expect = require("expect.js");

describe('beforeSave', function () {
	describe('beforeSaveTest1', function () {
    	it('should process group before saving', function (done) {
 			tests.getKeywords("A Group Name", {
      			success : function (message) {
          			done();
          			expect(message).to.be.eql(['a', 'group', 'name']);
      			}
  			});
		});
  	});
})

describe('beforeSave', function () {
	describe('beforeSaveTest2', function () {
    	it('should process group before saving', function (done) {
 			tests.getKeywords("The Best Group yet", {
      			success : function (message) {
          			done();
          			expect(message).to.be.eql(['best', 'group']);
      			}
  			});
		});
  	});
})



// var assert = require("assert")
// // require('../src/parse-mockdb');
// require('../node_modules/parse-mockdb/src/parse-mockdb.js');
// var Parse = require('parse').Parse;

// describe('Parse MockDB Test', function () {
//   beforeEach(function() {
//     Parse.MockDB.mockDB();
//   });
 
//   afterEach(function() {
//     Parse.MockDB.cleanUp();
//   });

//   // it ('should save keywords after saving group', function (done) {
//   // 		var Group = Parse.Object.extend("Group");
//   // 		var group = new Group();
//   // 		group.set("name", "A New Group");
//   // 		group.save().then(function(group) {
//   // 			var query = Parse.Query(Group);
//   // 			query.equalTo("name", "A New Group");
//   // 			return query.find().then(function(groups) {
//   // 				var keywords = groups[0].get("keywords");
//   // 				assert.equal("a", keywords.indexOf(0));
//   // 				assert.equal("new", keywords.indexOf(1));
//   // 				assert.equal("group", keywords.indexOf(2));
//   // 				done();
//   // 			});
//   // 		});
//   // });
 
//   it('should save and find an item', function (done) {
//     var Item = Parse.Object.extend("Item");
//     var item = new Item();
//     item.set("price", 30);
//     item.save().then(function(item) {
//       var query = new Parse.Query(Item);
//       query.equalTo("price", 30);
//       return query.find().then(function(items) {
//         assert(items[0].get("price") == 30);
//         done();
//       });
//     });
//   });
// });




