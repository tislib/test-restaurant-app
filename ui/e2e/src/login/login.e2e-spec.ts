import {LoginPage} from './login.po';

describe('login page', () => {
  let page: LoginPage;

  beforeEach(async () => {
    page = new LoginPage();

    await page.navigateTo();
  });

  it('should display welcome message', async () => {
    expect(await page.getTitleText()).toEqual('Welcome to Restaurant App');
  });

  it('should show error message while signing in with wrong username message', async () => {
    await page.writeEmail('admin@app.com');
    await page.writePassword('wrong_password');

    await page.clickSignIn();

    expect(await page.getNotificationMessage()).toEqual('username or password is wrong');
  });

  it('should show error success message while signing in with correct username message', async () => {
    await page.writeEmail('admin@app.com');
    await page.writePassword('admin123');

    await page.clickSignIn();

    expect(await page.getNotificationMessage()).toEqual('you are authenticated successfully');

    await page.clickLogout();
  });

});
