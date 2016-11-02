/*
 * There is an attached file. I need to view it using WebGL (threeJS preferred). I need to be able
 * to rotate a .obj file (attached) in a 3d space window, and I need to be able to click on the objects
 * surface, and return XYZ where in the global 3d space coordinate system the click collided with a surface. 
 */
function loadFile (url, container) {
	var dispatcher = new THREE.EventDispatcher;

	(new THREE.OBJLoader).load (url, function (group) {

		var renderer = new THREE.WebGLRenderer;
		container.appendChild (renderer.domElement);

		// center the group

		var box = new THREE.Box3;
		box.setFromObject (group);

		var sphere = box.getBoundingSphere ();
		group.position.copy (sphere.center).multiplyScalar (-1);

		group.traverse (function (object) {
			if (object.geometry && false) {
				var geometry = new THREE.Geometry;
				geometry.fromBufferGeometry (object.geometry);
				object.geometry = geometry;
			}
		});

		// create scene and controls

		var scene = new THREE.Scene;
		scene.add (group);

		scene.add (new THREE.AmbientLight (0x404040));

		var light = new THREE.DirectionalLight (0xffffff, 0.6);
		light.position.set (-1, 1, -1);
		scene.add (light);

		var camera = new THREE.PerspectiveCamera (75, 1, 0.1 * sphere.radius, 10 * sphere.radius);
		camera.position.z = -2 * sphere.radius;

		var render = function () {
			renderer.render (scene, camera);

			// inform them we rendered stuff

			dispatcher.dispatchEvent ({
				type: 'rendered'
			});
		};

		var discardClick = false;

		var controls = new THREE.OrbitControls (camera, renderer.domElement);
		controls.addEventListener ('start', function () {
			discardClick = false;
		});
		controls.addEventListener ('change', function () {
			discardClick = true;
			render ();
		});

		dispatcher.addEventListener ('resize', function (event) {
			var width = event.width || container.offsetWidth;
			var height = event.height || container.offsetHeight;
			camera.aspect = width / height;
			camera.updateProjectionMatrix ();
			renderer.setSize (width, height);
			render ();
		});

		var raycaster = new THREE.Raycaster;

		renderer.domElement.addEventListener ('click', function (event) {
			// remove orbit controls noise
			if (discardClick) return;

			var rect = renderer.domElement.getBoundingClientRect ();

			raycaster.ray.origin.set (0, 0, 0);
			camera.localToWorld (raycaster.ray.origin);
			raycaster.ray.direction.set (
				((event.clientX - rect.left) / rect.width) * 2 - 1,
				((rect.top - event.clientY) / rect.height) * 2 + 1,
			0.5).unproject (camera).sub (raycaster.ray.origin).normalize ();

			var intersects = raycaster.intersectObject (scene, true);
			if (intersects && intersects[0]) {

				// return world point

				dispatcher.dispatchEvent ({
					type: 'click',
					point: intersects[0].point
				});
			}
		});

		// add, remove and other manipulation tasks shall go there

		dispatcher.addEventListener ('add', function (event) {
			scene.add (event.object);
			render ();
		});

		// ready

		dispatcher.dispatchEvent ({
			type: 'ready'
		});
	});

	return dispatcher;
}