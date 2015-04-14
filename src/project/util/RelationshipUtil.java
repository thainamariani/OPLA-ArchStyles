package project.util;

import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.relationship.AbstractionRelationship;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;
import java.util.List;
import java.util.UUID;

public class RelationshipUtil {

    public static Element getUsedElementFromRelationship(Relationship relationship) {
        Element supplier = null;
        if (relationship instanceof UsageRelationship) {
            UsageRelationship usage = (UsageRelationship) relationship;
            supplier = usage.getSupplier();
        } else if (relationship instanceof DependencyRelationship) {
            DependencyRelationship dependency = (DependencyRelationship) relationship;
            supplier = dependency.getSupplier();
        } else if (relationship instanceof AbstractionRelationship) {
            AbstractionRelationship abstraction = (AbstractionRelationship) relationship;
            supplier = abstraction.getSupplier();
        } else if (relationship instanceof GeneralizationRelationship) {
            GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
            supplier = generalization.getParent();
        } else if (relationship instanceof RealizationRelationship) {
            RealizationRelationship realization = (RealizationRelationship) relationship;
            supplier = realization.getSupplier();
        } else if (relationship instanceof AssociationRelationship) {
            AssociationRelationship association = (AssociationRelationship) relationship;
            List<AssociationEnd> participants = association.getParticipants();
            if ((participants.get(0).isAggregation() == false) && (participants.get(1).isAggregation() == false) && (participants.get(0).isComposite() == false) && (participants.get(1).isComposite() == false)) {
                if ((participants.get(0).isNavigable() == true) && (participants.get(1).isNavigable() == false)) {
                    supplier = participants.get(0).getCLSClass();
                } else if ((participants.get(0).isNavigable() == false) && (participants.get(1).isNavigable() == true)) {
                    supplier = participants.get(1).getCLSClass();
                }
            }
        }
        return supplier;
    }

    public static Element getClientElementFromRelationship(Relationship relationship) {
        Element client = null;
        if (relationship instanceof UsageRelationship) {
            UsageRelationship usage = (UsageRelationship) relationship;
            client = usage.getClient();
        } else if (relationship instanceof DependencyRelationship) {
            DependencyRelationship dependency = (DependencyRelationship) relationship;
            client = dependency.getClient();
        } else if (relationship instanceof AbstractionRelationship) {
            AbstractionRelationship abstraction = (AbstractionRelationship) relationship;
            client = abstraction.getClient();
        } else if (relationship instanceof GeneralizationRelationship) {
            GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
            client = generalization.getChild();
        } else if (relationship instanceof RealizationRelationship) {
            RealizationRelationship realization = (RealizationRelationship) relationship;
            client = realization.getClient();
        } else if (relationship instanceof AssociationRelationship) {
            AssociationRelationship association = (AssociationRelationship) relationship;
            List<AssociationEnd> participants = association.getParticipants();
            if ((participants.get(0).isAggregation() == false) && (participants.get(1).isAggregation() == false) && (participants.get(0).isComposite() == false) && (participants.get(1).isComposite() == false)) {
                if ((participants.get(0).isNavigable() == true) && (participants.get(1).isNavigable() == false)) {
                    client = participants.get(1).getCLSClass();
                } else if ((participants.get(0).isNavigable() == false) && (participants.get(1).isNavigable() == true)) {
                    client = participants.get(0).getCLSClass();
                }
            }
        }
        return client;
    }

    public static boolean verifyAssociationRelationship(AssociationRelationship association) {
        boolean isUnidirectional = true;
        List<AssociationEnd> participants = association.getParticipants();
        //verifica se é composição ou agregação (então é dado como bidirecional)
        for (AssociationEnd participant : participants) {
            if ((participant.isAggregation() == true) || (participant.isComposite() == true)) {
                isUnidirectional = false;
            }
        }
        if (isUnidirectional) {
            //verifica se as duas pontas são false ou as duas são true (então é bidirecional)
            if (((participants.get(0).isNavigable() == false) && (participants.get(1).isNavigable() == false)) || ((participants.get(0).isNavigable() == true) && (participants.get(1).isNavigable() == true))) {
                isUnidirectional = false;
            }
        }
        return isUnidirectional;
    }

    public static Interface getImplementedInterface(Relationship relationship) {
        if (relationship instanceof RealizationRelationship) {
            RealizationRelationship realization = (RealizationRelationship) relationship;
            return realization.getSupplier() instanceof Interface ? (Interface) realization.getSupplier() : null;
        }
        return null;
    }

    public static Element getExtendedElement(Relationship relationship) {
        if (relationship instanceof GeneralizationRelationship) {
            GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
            return generalization.getParent();
        }
        return null;
    }

    public static Element getSubElement(Relationship relationship) {
        if (relationship instanceof GeneralizationRelationship) {
            GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
            return generalization.getChild();
        } else if (relationship instanceof RealizationRelationship) {
            RealizationRelationship realization = (RealizationRelationship) relationship;
            return realization.getClient();
        }
        return null;
    }

    public static void moveRelationship(Relationship relationship, Element client, Element supplier) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();
        if (relationship instanceof UsageRelationship) {
            UsageRelationship usage = (UsageRelationship) relationship;

            architecture.removeRelationship(usage);
            ElementUtil.verifyAndRemoveRequiredInterface(usage.getClient(), usage.getSupplier());

            usage.setSupplier(supplier);
            usage.setClient(client);
            architecture.addRelationship(usage);
            ElementUtil.addRequiredInterface(client, supplier);
        } else if (relationship instanceof DependencyRelationship) {
            DependencyRelationship dependency = (DependencyRelationship) relationship;

            architecture.removeRelationship(dependency);
            ElementUtil.verifyAndRemoveRequiredInterface(dependency.getClient(), dependency.getSupplier());

            dependency.setSupplier(supplier);
            dependency.setClient(client);
            architecture.addRelationship(dependency);
            ElementUtil.addRequiredInterface(client, supplier);
        }
    }

    public static RealizationRelationship createNewRealizationRelationship(String relationshipName, Element client, Element supplier) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

        RealizationRelationship realizationRelationship = new RealizationRelationship(client, supplier, relationshipName, UUID.randomUUID().toString());
        architecture.addRelationship(realizationRelationship);
        ElementUtil.addImplementedInterface(client, supplier);
        return realizationRelationship;
    }

    public static GeneralizationRelationship createNewGeneralizationRelationship(Element child, Element parent) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

        GeneralizationRelationship generalizationRelationship = new GeneralizationRelationship(parent, child, architecture.getRelationshipHolder(), UUID.randomUUID().toString());
        architecture.addRelationship(generalizationRelationship);
        ElementUtil.addImplementedInterface(child, parent);
        return generalizationRelationship;
    }

    public static UsageRelationship createNewUsageRelationship(String relationshipName, Element client, Element supplier) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

        UsageRelationship usage = new UsageRelationship(relationshipName, supplier, client, UUID.randomUUID().toString());
        architecture.addRelationship(usage);
        ElementUtil.addRequiredInterface(client, supplier);
        return usage;
    }

    public static AssociationRelationship createNewAggregationRelationship(String name, Element aggregator, Element aggregated) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

        AssociationRelationship associationRelationship = new AssociationRelationship(aggregator, aggregated);
        associationRelationship.setName(name);
        associationRelationship.getParticipants().get(1).setAggregation("shared");
        architecture.addRelationship(associationRelationship);
        ElementUtil.addRequiredInterface(aggregator, aggregated);
        return associationRelationship;
    }

    private RelationshipUtil() {
    }

}
