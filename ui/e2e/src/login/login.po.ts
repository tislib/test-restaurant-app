import { browser, by, element } from 'protractor';

export class LoginPage {
  async navigateTo(): Promise<unknown> {
    return browser.get(browser.baseUrl + 'login');
  }

  async getTitleText(): Promise<string> {
    return element(by.css('.e2e-login-text')).getText();
  }
}
