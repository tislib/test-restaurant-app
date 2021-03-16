import {browser, by, element} from 'protractor';

export class LoginPage {
  async navigateTo(): Promise<unknown> {
    return browser.get(browser.baseUrl + 'login');
  }

  async getTitleText(): Promise<string> {
    return element(by.css('.e2e-login-text')).getText();
  }

  async getNotificationMessage(): Promise<string> {
    return element(by.css('.notifier__notification-message')).getText();
  }

  async writeEmail(email: string): Promise<void> {
    return element(by.css('.e2e-email-field')).sendKeys(email);
  }

  async writePassword(password: string): Promise<void> {
    return element(by.css('.e2e-password-field')).sendKeys(password);
  }

  async clickSignIn(): Promise<void> {
    return element(by.css('.e2e-signin-button')).click();
  }

  async clickLogout(): Promise<void> {
    return element(by.css('.e2e-logout-button')).click();
  }
}
