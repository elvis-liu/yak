Yet Another KunWu
=====

This would be a true lightweight version IoC container.

Planned features:
   * Service module based configuration, each module defines the services it provides
   * Constructor injector
      * Picking up the most suitable constructor
      * Manually specify constructor to use
   * Factory method injector
   * Setter method injector
   * Service instance cache support (By delegation)
   * Container scope
   * Easy service mock support
   * Dependency graph report

Design considerations:
   * Implementation doesn't need to have any dependencies on the container, even annotations.
   * All the services must be interface or abstract class, to enforce Interface Oriented Programming.
   * Use Java enum instead of String as key for locating alternative services (String link instead of weak).
   * It is not possible to clearly show the dependency graph only by the configuration,
     since the interdependence between module services are defined by the implementations (service providers).
     But provides both the implementation code and configuration, it should be possible to generate the dependency graph.
   * No circular dependency support, which means there must be at least one entry service for each module.
     It might be worthy to add some template entry services and implementations, like CommandLineEntryService, ServletEntryService, etc
   * The container must be simple and small enough to run on Android platform
