public void goTo${transition}(View view) {
	Intent intent = new Intent(this, ${transition}.class);
	startActivity(intent);
}