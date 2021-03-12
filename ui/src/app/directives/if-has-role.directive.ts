import {Directive, Input, TemplateRef, ViewContainerRef} from '@angular/core';
import {AuthenticationService} from '../service/authentication-service';

@Directive({
  selector: '[appIfHasRole]'
})
export class IfHasRoleDirective {

  @Input()
  public set appIfHasRole(role: string) {
    this.authService.getToken().subscribe(resp => {
      if (role.split(',').some(item => item === resp.user.role)) {
        this.viewContainerRef.createEmbeddedView(this.templateRef);
      } else {
        this.viewContainerRef.clear();
      }
    });
  }

  constructor(private templateRef: TemplateRef<any>,
              private viewContainerRef: ViewContainerRef,
              private authService: AuthenticationService) {
  }

}
