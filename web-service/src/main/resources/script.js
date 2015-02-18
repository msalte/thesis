var mainController = function($scope, $http) {
	
	$scope.title = '';					// The title of the web page
	$scope.data = {};					// All the data returned by the call to
										// '/api'
	
	$scope.functionNames = [];			// An array of available function names
	$scope.selectedFunction = {};		// The currently selected function
	
	$scope.initialForm = {};			// The contents of the form initially,
										// i.e. empty
	$scope.initialResponse = {};		// The contents of the response
										// initially, i.e. empty
	
	$scope.form = {};					// The contents of the current form
	$scope.response = {};				// The contents of the current response
	
	// This function will run on page load and the entire
	// web page is rendered from its result
	$http.get('/api').success(function(data, status, headers, config) {
		$scope.title = data.message;
		$scope.data = data.content;
		
		for(var i=0;i<data.content.length;i++) {
			$scope.functionNames.push(data.content[i].function);					
		}
	});
	
	// This function will submit the current form and send
	// a HTTP request (GET/POST) to the selected web service function
	$scope.submitForm = function() {
		var method = $scope.selectedFunction.method;
		var dataToSend = {};
		
		if($scope.selectedFunction.params != null) {
			for(var i=0;i<$scope.selectedFunction.params.length;i++) {
				var param = $scope.selectedFunction.params[i];
				var arg = $scope.form[param];

				// return if required argument is empty
				if(typeof arg === 'undefined') {
					return;
				}

				dataToSend[param] = arg;
			}
		}
		
		var url = "/" + $scope.selectedFunction.function;
		
		$http({
		    method: method,
		    url: url,
		    data: $.param(dataToSend),
		    headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
		}).success(function(data) {
		    $scope.response = data;
		});
		
		$scope.resetForm();
	};
	
	// This function will reset the current
	// form back to its initial state, i.e. empty
	$scope.resetForm = function() {
		$scope.form = angular.copy($scope.initialForm);
	};
	
	// This function will reset the current
	// response back to its initial state, i.e. empty
	$scope.resetResponse = function() {
		$scope.response = angular.copy($scope.initialResponse);
	};
	
	// This function will set the selected function
	// based on the user's choice in the drop down menu
	$scope.setSelectedFunction = function(func) {
		$scope.resetForm();
		$scope.resetResponse();
		
		for(var i = 0;i < $scope.data.length;i++) {
			var obj = $scope.data[i];
			
			var ok = obj.function == func;
			
			if(ok) {
				$scope.selectedFunction.function = obj.function;
				$scope.selectedFunction.params = obj.params;
				$scope.selectedFunction.args = obj.args;
				$scope.selectedFunction.details = obj.details;
				$scope.selectedFunction.method = obj.method;
			}
		}
	};
};